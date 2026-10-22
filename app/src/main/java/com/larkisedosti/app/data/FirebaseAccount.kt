package com.larkisedosti.app.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.larkisedosti.app.model.Countries
import com.larkisedosti.app.model.Country
import com.larkisedosti.app.model.Gender
import com.larkisedosti.app.model.PhotoStatus
import com.larkisedosti.app.model.ProfilePhoto
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val uid: String,
    val username: String,
    val displayName: String,
    val shortId: Long,
    val dateOfBirth: String,
    val gender: Gender,
    val country: Country,
    val photos: List<ProfilePhoto?>,
    val coins: Int,
    val level: Int,
    val isOnline: Boolean,
    val lastSeen: Long,
    val createdAt: Long,
    val onboardingCompleted: Boolean,
    val isGoogleLinked: Boolean,
)

sealed interface AccountGate {
    data class Discover(val profile: UserProfile) : AccountGate
    data object Onboarding : AccountGate
}

sealed interface GoogleAuthResult {
    data class Ready(val profile: UserProfile?) : GoogleAuthResult
    data object Cancelled : GoogleAuthResult
    data class Failed(val message: String) : GoogleAuthResult
}

sealed interface LinkGoogleResult {
    data object Linked : LinkGoogleResult
    data object Collision : LinkGoogleResult
    data object Cancelled : LinkGoogleResult
    data class Failed(val message: String) : LinkGoogleResult
}

class UsernameTakenException : Exception()

class FirebaseAccount(context: Context) {
    private val appContext = context.applicationContext
    private val credentials = CredentialManager.create(appContext)
    private var pendingGoogleCredential: com.google.firebase.auth.AuthCredential? = null
    private val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    suspend fun enter(): AccountGate {
        if (!ready()) {
            error("Place google-services.json in the app folder for com.larkisedosti.app, then rebuild.")
        }
        val user = auth.currentUser ?: auth.signInAnonymously().await().user
            ?: error("Couldn't start a session.")
        val profile = loadProfile(user.uid)
        return if (profile?.onboardingCompleted == true) {
            AccountGate.Discover(profile)
        } else {
            AccountGate.Onboarding
        }
    }

    suspend fun isUsernameAvailable(username: String): Boolean {
        if (!username.matches(usernamePattern)) return false
        val snapshot = db.collection(USERNAMES).document(username).get().await()
        if (!snapshot.exists()) return true
        return snapshot.getString("uid") == auth.currentUser?.uid
    }

    suspend fun createProfile(
        username: String,
        displayName: String,
        dateOfBirth: String,
        gender: Gender,
        country: Country,
        photos: List<ProfilePhoto?>,
    ): UserProfile {
        val user = auth.currentUser ?: auth.signInAnonymously().await().user
            ?: error("Couldn't start a session.")
        val uid = user.uid
        val cleanName = username.trim().lowercase()
        if (!cleanName.matches(usernamePattern)) error("Username doesn't match the rules.")
        keepPhoto(photos.firstOrNull()?.uri)
        val now = System.currentTimeMillis()
        val linked = user.isGoogleLinked()
        val userRef = db.collection(USERS).document(uid)
        val nameRef = db.collection(USERNAMES).document(cleanName)
        val counterRef = db.collection(COUNTERS).document("users")
        val shortId = try {
            db.runTransaction { transaction ->
                val reserved = transaction.get(nameRef)
                val owner = reserved.getString("uid")
                if (reserved.exists() && owner != uid) throw UsernameTakenException()
                val counter = transaction.get(counterRef)
                val next = if (counter.exists()) counter.getLong("next") ?: 1L else 1L
                transaction.set(counterRef, mapOf("next" to next + 1))
                transaction.set(nameRef, mapOf("uid" to uid))
                transaction.set(
                    userRef,
                    mapOf(
                        "uid" to uid,
                        "username" to cleanName,
                        "displayName" to displayName.trim(),
                        "shortId" to next,
                        "dateOfBirth" to dateOfBirth,
                        "gender" to gender.name,
                        "country" to country.name,
                        "photos" to encodePhotos(photos),
                        "coins" to STARTING_COINS,
                        "level" to 1,
                        "isOnline" to true,
                        "lastSeen" to now,
                        "createdAt" to now,
                        "onboardingCompleted" to true,
                        "isGoogleLinked" to linked,
                    ),
                )
                next
            }.await()
        } catch (error: Exception) {
            if (error.hasCause<UsernameTakenException>()) throw UsernameTakenException()
            throw error
        }
        return UserProfile(
            uid = uid,
            username = cleanName,
            displayName = displayName.trim(),
            shortId = shortId,
            dateOfBirth = dateOfBirth,
            gender = gender,
            country = country,
            photos = photos,
            coins = STARTING_COINS,
            level = 1,
            isOnline = true,
            lastSeen = now,
            createdAt = now,
            onboardingCompleted = true,
            isGoogleLinked = linked,
        )
    }

    suspend fun updateProfile(
        displayName: String,
        gender: Gender,
        country: Country,
        photos: List<ProfilePhoto?>,
    ) {
        val uid = auth.currentUser?.uid ?: return
        photos.forEach { keepPhoto(it?.uri) }
        db.collection(USERS).document(uid).update(
            mapOf(
                "displayName" to displayName.trim(),
                "gender" to gender.name,
                "country" to country.name,
                "photos" to encodePhotos(photos),
            ),
        ).await()
    }

    fun setOnline(online: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        db.collection(USERS).document(uid).update(
            mapOf(
                "isOnline" to online,
                "lastSeen" to System.currentTimeMillis(),
            ),
        )
    }

    suspend fun signInWithGoogle(activity: Activity): GoogleAuthResult {
        val credential = try {
            googleCredential(activity)
        } catch (error: GetCredentialCancellationException) {
            return GoogleAuthResult.Cancelled
        } catch (error: NoCredentialException) {
            return GoogleAuthResult.Failed("No Google account was found on this phone.")
        } catch (error: Exception) {
            return GoogleAuthResult.Failed(googleFailure(error))
        }
        return try {
            auth.signInWithCredential(credential).await()
            GoogleAuthResult.Ready(auth.currentUser?.uid?.let { loadProfile(it) })
        } catch (error: Exception) {
            GoogleAuthResult.Failed(googleFailure(error))
        }
    }

    suspend fun linkGoogle(activity: Activity): LinkGoogleResult {
        val user = auth.currentUser ?: return LinkGoogleResult.Failed("Sign in first.")
        if (user.isGoogleLinked()) return LinkGoogleResult.Linked
        val credential = try {
            googleCredential(activity)
        } catch (error: GetCredentialCancellationException) {
            return LinkGoogleResult.Cancelled
        } catch (error: NoCredentialException) {
            return LinkGoogleResult.Failed("No Google account was found on this phone.")
        } catch (error: Exception) {
            return LinkGoogleResult.Failed(googleFailure(error))
        }
        return try {
            user.linkWithCredential(credential).await()
            pendingGoogleCredential = null
            val uid = auth.currentUser?.uid
            if (uid != null && loadProfile(uid) != null) {
                db.collection(USERS).document(uid).update("isGoogleLinked", true).await()
            }
            LinkGoogleResult.Linked
        } catch (error: FirebaseAuthUserCollisionException) {
            pendingGoogleCredential = credential
            LinkGoogleResult.Collision
        } catch (error: Exception) {
            LinkGoogleResult.Failed(googleFailure(error))
        }
    }

    suspend fun signIntoLinkedGoogle(): UserProfile? {
        val credential = pendingGoogleCredential ?: error("That Google sign-in expired. Try again.")
        auth.signInWithCredential(credential).await()
        pendingGoogleCredential = null
        return auth.currentUser?.uid?.let { loadProfile(it) }
    }

    suspend fun deleteAccount() {
        val uid = auth.currentUser?.uid ?: return
        val profile = loadProfile(uid)
        db.collection(USERS).document(uid).delete().await()
        if (profile != null && profile.username.isNotBlank()) {
            db.collection(USERNAMES).document(profile.username).delete().await()
        }
        try {
            auth.currentUser?.delete()?.await()
        } catch (error: FirebaseAuthRecentLoginRequiredException) {
            auth.signOut()
        }
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }
    }

    suspend fun logOut() {
        auth.signOut()
        pendingGoogleCredential = null
        auth.signInAnonymously().await()
    }

    private fun ready(): Boolean {
        val appId = appContext.resources.getIdentifier("google_app_id", "string", appContext.packageName)
        if (appId == 0) return false
        if (FirebaseApp.getApps(appContext).isEmpty()) {
            FirebaseApp.initializeApp(appContext)
        }
        return FirebaseApp.getApps(appContext).isNotEmpty()
    }

    fun keepPhoto(uri: String?) {
        if (uri.isNullOrBlank() || !uri.startsWith("content:")) return
        try {
            appContext.contentResolver.takePersistableUriPermission(
                android.net.Uri.parse(uri),
                Intent.FLAG_GRANT_READ_URI_PERMISSION,
            )
        } catch (_: Exception) {
            // The picker may not grant a persistable read. The address is still saved.
        }
    }

    private suspend fun loadProfile(uid: String): UserProfile? {
        val snapshot = db.collection(USERS).document(uid).get().await()
        if (!snapshot.exists()) return null
        val data = snapshot.data ?: return null
        if (data["onboardingCompleted"] != true) return null
        return parseProfile(uid, data)
    }

    private fun parseProfile(uid: String, data: Map<String, Any>): UserProfile {
        val displayName = data["displayName"] as? String ?: error("missing name")
        val username = data["username"] as? String ?: error("missing username")
        val dateOfBirth = data["dateOfBirth"] as? String ?: error("missing birthday")
        val gender = Gender.valueOf(data["gender"] as? String ?: error("missing gender"))
        val country = Countries.find { it.name == data["country"] } ?: error("missing country")
        val completed = data["onboardingCompleted"] as? Boolean ?: false
        if (displayName.isBlank() || username.isBlank() || !completed) error("incomplete")
        return UserProfile(
            uid = uid,
            username = username,
            displayName = displayName,
            shortId = (data["shortId"] as? Number)?.toLong() ?: 0L,
            dateOfBirth = dateOfBirth,
            gender = gender,
            country = country,
            photos = decodePhotos(data["photos"]),
            coins = (data["coins"] as? Number)?.toInt() ?: STARTING_COINS,
            level = (data["level"] as? Number)?.toInt() ?: 1,
            isOnline = data["isOnline"] as? Boolean ?: false,
            lastSeen = (data["lastSeen"] as? Number)?.toLong() ?: 0L,
            createdAt = (data["createdAt"] as? Number)?.toLong() ?: 0L,
            onboardingCompleted = true,
            isGoogleLinked = data["isGoogleLinked"] as? Boolean ?: false,
        )
    }

    private suspend fun googleCredential(activity: Activity): com.google.firebase.auth.AuthCredential {
        val webClientId = webClientId()
        val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()
        val result = credentials.getCredential(activity, request)
        val credential = result.credential
        if (credential !is CustomCredential || credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            error("Google didn't return an account.")
        }
        val token = GoogleIdTokenCredential.createFrom(credential.data).idToken
        return GoogleAuthProvider.getCredential(token, null)
    }

    private fun webClientId(): String {
        val id = appContext.resources.getIdentifier("default_web_client_id", "string", appContext.packageName)
        if (id == 0) {
            error("Add google-services.json for com.larkisedosti.app, then rebuild. Google sign-in also needs a Web client ID.")
        }
        return appContext.getString(id)
    }

    private fun googleFailure(error: Exception): String {
        val text = error.message.orEmpty()
        return if (text.contains("google-services", ignoreCase = true) || text.contains("Web client", ignoreCase = true)) {
            text
        } else {
            "Google sign-in didn't finish. Check your connection and try again."
        }
    }

    private fun com.google.firebase.auth.FirebaseUser.isGoogleLinked(): Boolean {
        return providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }
    }

    private fun encodePhotos(photos: List<ProfilePhoto?>): List<Map<String, String>> {
        return photos.mapNotNull { photo ->
            photo?.takeIf { it.uri.isNotBlank() }?.let {
                mapOf("uri" to it.uri, "status" to it.status.name)
            }
        }
    }

    private fun decodePhotos(raw: Any?): List<ProfilePhoto?> {
        val parsed = (raw as? List<*>)?.mapNotNull { item ->
            when (item) {
                is String -> item.takeIf { it.isNotBlank() }?.let { ProfilePhoto(it, PhotoStatus.Pending) }
                is Map<*, *> -> {
                    val uri = item["uri"] as? String ?: return@mapNotNull null
                    if (uri.isBlank()) return@mapNotNull null
                    val status = runCatching {
                        PhotoStatus.valueOf(item["status"] as? String ?: PhotoStatus.Pending.name)
                    }.getOrDefault(PhotoStatus.Pending)
                    ProfilePhoto(uri, status)
                }
                else -> null
            }
        }.orEmpty()
        return List(3) { index -> parsed.getOrNull(index) }
    }

    companion object {
        const val STARTING_COINS = 50
        private const val USERS = "users"
        private const val USERNAMES = "usernames"
        private const val COUNTERS = "counters"
        private val usernamePattern = Regex("^[a-z][a-z0-9_]{2,19}$")
    }
}

private inline fun <reified T : Throwable> Throwable.hasCause(): Boolean {
    var current: Throwable? = this
    while (current != null) {
        if (current is T) return true
        current = current.cause
    }
    return false
}
