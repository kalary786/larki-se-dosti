package com.larkisedosti.app.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File
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
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.storage.FirebaseStorage
import com.larkisedosti.app.model.CatalogGift
import com.larkisedosti.app.model.LedgerEntry
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageMetadata
import com.larkisedosti.app.model.Countries
import com.larkisedosti.app.model.Country
import com.larkisedosti.app.model.Gender
import com.larkisedosti.app.model.PhotoStatus
import com.larkisedosti.app.model.ProfilePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
    val isTestUser: Boolean = false,
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

sealed interface GiftCallResult {
    data class Sent(val balance: Int) : GiftCallResult
    data object Insufficient : GiftCallResult
    data class Failed(val message: String) : GiftCallResult
}

sealed interface MessageCallResult {
    data class Sent(val coins: Int, val freeMessagesLeft: Int) : MessageCallResult
    data object NoFreeMessages : MessageCallResult
    data class Failed(val message: String) : MessageCallResult
}

sealed interface CallChargeResult {
    data class Charged(val balance: Int) : CallChargeResult
    data object Insufficient : CallChargeResult
    data object Failed : CallChargeResult
}

data class DiscoverPage(
    val profiles: List<UserProfile>,
    val hasMore: Boolean,
)

class FirebaseAccount(context: Context) {
    private val appContext = context.applicationContext
    private val credentials = CredentialManager.create(appContext)
    private var pendingGoogleCredential: com.google.firebase.auth.AuthCredential? = null
    private val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage
        get() = FirebaseStorage.getInstance()
    private val functions: FirebaseFunctions
        get() = FirebaseFunctions.getInstance("asia-south1")
    private val presence = Presence { auth.currentUser?.uid }

    fun bindPresence(
        onUpdate: (String, Boolean, Long) -> Unit,
        onClear: (String) -> Unit,
    ) {
        presence.onUpdate = onUpdate
        presence.onClear = onClear
    }

    fun watchPresence(scope: String, ids: Collection<String>) {
        if (!ready()) return
        presence.watch(scope, ids)
    }

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
        if (!ready()) error("Firebase isn't ready.")
        registerThisDevice()
        val cleanName = username.trim().lowercase()
        if (!cleanName.matches(usernamePattern)) error("Username doesn't match the rules.")
        keepPhoto(photos.firstOrNull()?.url)
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
                        "coins" to 0,
                        "giftsSentCount" to 0,
                        "giftsReceivedCount" to 0,
                        "freeMessagesUsed" to 0,
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
            coins = 0,
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
    ) {
        val uid = auth.currentUser?.uid ?: return
        db.collection(USERS).document(uid).update(
            mapOf(
                "displayName" to displayName.trim(),
                "gender" to gender.name,
                "country" to country.name,
            ),
        ).await()
    }

    fun saveLocalPhoto(uid: String, index: Int, source: Uri): ProfilePhoto {
        if (index !in 0..2) error("Photo slot is out of range.")
        val bytes = PhotoFiles.jpeg(appContext, source)
        val dir = File(appContext.filesDir, "profile-photos/$uid").apply { mkdirs() }
        val file = File(dir, "$index.jpg")
        file.writeBytes(bytes)
        val stored = Uri.fromFile(file).toString()
        val slots = readLocalSlots(uid).toMutableList()
        slots[index] = stored
        writeLocalSlots(uid, slots)
        return ProfilePhoto("local-$index", stored, PhotoStatus.Pending)
    }

    fun localPhotos(uid: String): List<ProfilePhoto?> {
        return readLocalSlots(uid).mapIndexed { index, stored ->
            val path = stored?.let { Uri.parse(it).path } ?: return@mapIndexed null
            if (!File(path).exists()) null else ProfilePhoto("local-$index", stored, PhotoStatus.Pending)
        }
    }

    fun removeLocalPhoto(uid: String, index: Int) {
        if (index !in 0..2) return
        val slots = readLocalSlots(uid).toMutableList()
        slots[index]?.let { stored -> Uri.parse(stored).path?.let { File(it).delete() } }
        slots[index] = null
        writeLocalSlots(uid, slots)
    }

    fun deleteLocalPhotos(uid: String) {
        if (uid.isBlank()) return
        File(appContext.filesDir, "profile-photos/$uid").deleteRecursively()
        photoPrefs().edit().remove(uid).apply()
    }

    suspend fun savePhotos(photos: List<ProfilePhoto?>) {
        val uid = auth.currentUser?.uid ?: error("Sign in first.")
        db.collection(USERS).document(uid).update("photos", encodePhotos(photos)).await()
    }

    suspend fun uploadJpeg(photoId: String, source: Uri, onProgress: (Float) -> Unit): String {
        if (!ready()) error("Firebase isn't ready.")
        val uid = auth.currentUser?.uid ?: error("Sign in first.")
        val bytes = withContext(Dispatchers.IO) { PhotoFiles.jpeg(appContext, source) }
        onProgress(0.02f)
        val ref = storage.reference.child("users/$uid/photos/$photoId.jpg")
        val metadata = StorageMetadata.Builder().setContentType("image/jpeg").build()
        val task = ref.putBytes(bytes, metadata)
        task.addOnProgressListener { snapshot ->
            val total = snapshot.totalByteCount.coerceAtLeast(1L)
            onProgress((snapshot.bytesTransferred.toFloat() / total.toFloat()).coerceIn(0.02f, 1f))
        }
        task.await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun deleteJpeg(photoId: String) {
        if (!ready()) return
        val uid = auth.currentUser?.uid ?: return
        try {
            storage.reference.child("users/$uid/photos/$photoId.jpg").delete().await()
        } catch (error: Exception) {
            var current: Throwable? = error
            var storageError: StorageException? = null
            while (current != null) {
                if (current is StorageException) storageError = current
                current = current.cause
            }
            if (storageError == null || storageError.errorCode != StorageException.ERROR_OBJECT_NOT_FOUND) throw error
        }
    }

    private var discoverCursor: com.google.firebase.firestore.DocumentSnapshot? = null

    suspend fun fetchDiscoverPage(gender: String?, country: String?, reset: Boolean): DiscoverPage {
        if (!ready()) error("Firebase isn't ready.")
        if (reset) discoverCursor = null
        var query: com.google.firebase.firestore.Query = db.collection(USERS)
            .whereEqualTo("onboardingCompleted", true)
        if (!gender.isNullOrBlank()) query = query.whereEqualTo("gender", gender)
        if (!country.isNullOrBlank()) query = query.whereEqualTo("country", country)
        query = query
            .orderBy("isOnline", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .orderBy("lastSeen", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(20)
        discoverCursor?.let { cursor -> query = query.startAfter(cursor) }
        val snapshot = query.get().await()
        discoverCursor = snapshot.documents.lastOrNull()
        val profiles = snapshot.documents.mapNotNull { document ->
            val data = document.data ?: return@mapNotNull null
            runCatching { parseProfile(document.id, data) }.getOrNull()
        }
        return DiscoverPage(profiles, snapshot.size() == 20)
    }

    suspend fun seedTestUsers() {
        callTestUsers("seedDiscoverUsers")
    }

    suspend fun deleteTestUsers() {
        callTestUsers("deleteDiscoverUsers")
    }

    private suspend fun callTestUsers(name: String) {
        if (!ready()) error("Firebase isn't ready.")
        try {
            functions.getHttpsCallable(name).call(emptyMap<String, Any>()).await()
        } catch (error: Exception) {
            var current: Throwable? = error
            while (current != null) {
                if (current is FirebaseFunctionsException &&
                    (current.code == FirebaseFunctionsException.Code.NOT_FOUND ||
                        current.code == FirebaseFunctionsException.Code.UNAVAILABLE)
                ) {
                    error("Deploy the Cloud Functions first, then try again.")
                }
                current = current.cause
            }
            throw error
        }
    }

    fun listenStats(uid: String, onUpdate: (coins: Int, giftsSent: Int?, freeMessagesUsed: Int) -> Unit): ListenerRegistration? {
        if (!ready() || uid.isBlank()) return null
        return db.collection(USERS).document(uid).addSnapshotListener { snap, _ ->
            if (snap == null || !snap.exists()) return@addSnapshotListener
            val coins = snap.getLong("coins")?.toInt() ?: return@addSnapshotListener
            val sent = snap.getLong("giftsSentCount")?.toInt()
            val used = snap.getLong("freeMessagesUsed")?.toInt() ?: 0
            onUpdate(coins, sent, used)
        }
    }

    fun listenAppSettings(onUpdate: (freeMessages: Int, messageCost: Int, rewardAdFreeMessages: Int) -> Unit): ListenerRegistration? {
        if (!ready()) return null
        return db.collection("config").document("app").addSnapshotListener { snap, _ ->
            if (snap == null || !snap.exists()) return@addSnapshotListener
            val free = snap.getLong("freeMessages")?.toInt() ?: return@addSnapshotListener
            val cost = snap.getLong("messageCost")?.toInt() ?: return@addSnapshotListener
            val reward = snap.getLong("rewardAdFreeMessages")?.toInt() ?: return@addSnapshotListener
            if (free <= 0 || cost <= 0 || reward <= 0) return@addSnapshotListener
            onUpdate(free, cost, reward)
        }
    }

    fun listenGifts(onUpdate: (List<CatalogGift>) -> Unit): ListenerRegistration? {
        if (!ready()) return null
        return db.collection("gifts").addSnapshotListener { snap, _ ->
            val gifts = snap?.documents?.mapNotNull { doc ->
                val name = doc.getString("name") ?: return@mapNotNull null
                val emoji = doc.getString("emoji") ?: return@mapNotNull null
                val price = doc.getLong("price")?.toInt() ?: return@mapNotNull null
                if (doc.getBoolean("active") == false || price <= 0) return@mapNotNull null
                CatalogGift(
                    id = doc.id,
                    name = name,
                    emoji = emoji,
                    price = price,
                    sortOrder = doc.getLong("sortOrder")?.toInt() ?: 0,
                )
            }.orEmpty().sortedBy { it.sortOrder }
            onUpdate(gifts)
        }
    }

    fun listenLedger(uid: String, onUpdate: (List<LedgerEntry>) -> Unit): ListenerRegistration? {
        if (!ready() || uid.isBlank()) return null
        return db.collection(USERS).document(uid).collection("coinLedger")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snap, _ ->
                val rows = snap?.documents?.map { doc ->
                    LedgerEntry(
                        id = doc.id,
                        type = doc.getString("type") ?: "admin",
                        amount = doc.getLong("amount")?.toInt() ?: 0,
                        balanceAfter = doc.getLong("balanceAfter")?.toInt() ?: 0,
                        createdAt = doc.getTimestamp("createdAt")?.toDate()?.time ?: 0L,
                    )
                }.orEmpty()
                onUpdate(rows)
            }
    }

    fun listenBlocks(uid: String, onUpdate: (List<String>) -> Unit): ListenerRegistration? {
        if (!ready() || uid.isBlank()) return null
        return db.collection(USERS).document(uid).collection("blocks")
            .addSnapshotListener { snap, _ ->
                onUpdate(snap?.documents?.map { it.id }.orEmpty())
            }
    }

    fun listenBlockedBy(uid: String, onUpdate: (List<String>) -> Unit): ListenerRegistration? {
        if (!ready() || uid.isBlank()) return null
        return db.collection(USERS).document(uid).collection("blockedBy")
            .addSnapshotListener { snap, _ ->
                onUpdate(snap?.documents?.map { it.id }.orEmpty())
            }
    }

    fun saveBlock(uid: String, otherUid: String, blocked: Boolean) {
        if (!ready() || uid.isBlank() || otherUid.isBlank() || uid == otherUid) return
        val stamp = mapOf("createdAt" to System.currentTimeMillis())
        val mine = db.collection(USERS).document(uid).collection("blocks").document(otherUid)
        val theirs = db.collection(USERS).document(otherUid).collection("blockedBy").document(uid)
        if (blocked) {
            mine.set(stamp)
            theirs.set(stamp)
        } else {
            mine.delete()
            theirs.delete()
        }
    }

    suspend fun ensureGiftCatalog() {
        if (!ready()) return
        try {
            functions.getHttpsCallable("ensureGiftCatalog").call(emptyMap<String, Any>()).await()
        } catch (_: Exception) {
            // The sheet stays empty until the catalog can be written.
        }
    }

    suspend fun sendGift(receiverUid: String, giftId: String, chatId: String): GiftCallResult {
        if (!ready()) return GiftCallResult.Failed("Firebase isn't ready.")
        return try {
            val result = functions.getHttpsCallable("sendGift").call(
                hashMapOf(
                    "receiverUid" to receiverUid,
                    "giftId" to giftId,
                    "chatId" to chatId,
                ),
            ).await()
            val data = result.getData() as? Map<*, *>
            val balance = (data?.get("balance") as? Number)?.toInt()
                ?: return GiftCallResult.Failed("The gift could not be sent. Try again.")
            GiftCallResult.Sent(balance)
        } catch (error: Exception) {
            when (functionCode(error)) {
                "insufficient_coins" -> GiftCallResult.Insufficient
                "blocked" -> GiftCallResult.Failed("You can't send a gift to this person.")
                "rate_limited" -> GiftCallResult.Failed("You can send up to 20 gifts a minute.")
                "user_not_found" -> GiftCallResult.Failed("That person is not available.")
                "gift_not_found", "gift_unavailable" -> GiftCallResult.Failed("That gift is not available.")
                "same_user" -> GiftCallResult.Failed("You can't send a gift to yourself.")
                else -> GiftCallResult.Failed("The gift could not be sent. Try again.")
            }
        }
    }

    suspend fun sendMessage(chatId: String, receiverUid: String, text: String, messageId: String): MessageCallResult {
        if (!ready()) return MessageCallResult.Failed("Firebase isn't ready.")
        return try {
            val result = functions.getHttpsCallable("sendMessage").call(
                hashMapOf(
                    "chatId" to chatId,
                    "receiverUid" to receiverUid,
                    "text" to text,
                    "messageId" to messageId,
                ),
            ).await()
            val data = result.getData() as? Map<*, *>
            val coins = (data?.get("coins") as? Number)?.toInt()
                ?: return MessageCallResult.Failed("The message could not be sent. Try again.")
            val left = (data["freeMessagesLeft"] as? Number)?.toInt() ?: 0
            MessageCallResult.Sent(coins, left)
        } catch (error: Exception) {
            when (functionCode(error)) {
                "no_free_messages" -> MessageCallResult.NoFreeMessages
                "blocked" -> MessageCallResult.Failed("You can't message this person.")
                "rate_limited" -> MessageCallResult.Failed("You can send up to 30 messages a minute.")
                "user_not_found" -> MessageCallResult.Failed("That person is not available.")
                "same_user" -> MessageCallResult.Failed("You can't message yourself.")
                else -> MessageCallResult.Failed("The message could not be sent. Try again.")
            }
        }
    }

    suspend fun chargeCall(otherUid: String): CallChargeResult {
        if (!ready()) return CallChargeResult.Failed
        return try {
            val result = functions.getHttpsCallable("chargeCall").call(
                hashMapOf("otherUid" to otherUid),
            ).await()
            val data = result.getData() as? Map<*, *>
            val balance = (data?.get("balance") as? Number)?.toInt() ?: return CallChargeResult.Failed
            CallChargeResult.Charged(balance)
        } catch (error: Exception) {
            if (functionCode(error) == "insufficient_coins") CallChargeResult.Insufficient else CallChargeResult.Failed
        }
    }

    fun chatIdFor(a: String, b: String): String = listOf(a, b).sorted().joinToString("_")

    private fun functionCode(error: Exception): String {
        var current: Throwable? = error
        while (current != null) {
            if (current is FirebaseFunctionsException) return current.message.orEmpty()
            current = current.cause
        }
        return ""
    }

    fun setOnline(online: Boolean) {
        if (!ready()) return
        if (auth.currentUser?.uid == null) return
        presence.setWanted(online)
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
        presence.clear()
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
        presence.clear()
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
        if (FirebaseApp.getApps(appContext).isEmpty()) return false
        installAppCheck()
        connectEmulators()
        return true
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
            coins = (data["coins"] as? Number)?.toInt() ?: 0,
            level = (data["level"] as? Number)?.toInt() ?: 1,
            isOnline = data["isOnline"] as? Boolean ?: false,
            lastSeen = (data["lastSeen"] as? Number)?.toLong() ?: 0L,
            createdAt = (data["createdAt"] as? Number)?.toLong() ?: 0L,
            onboardingCompleted = true,
            isGoogleLinked = data["isGoogleLinked"] as? Boolean ?: false,
            isTestUser = data["isTestUser"] as? Boolean ?: false,
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

    private fun photoPrefs() = appContext.getSharedPreferences("larki_local_photos", Context.MODE_PRIVATE)

    private fun readLocalSlots(uid: String): List<String?> {
        val raw = photoPrefs().getString(uid, null) ?: return listOf(null, null, null)
        val parts = raw.split("\n")
        return List(3) { index -> parts.getOrNull(index)?.takeIf { it.isNotBlank() } }
    }

    private fun writeLocalSlots(uid: String, slots: List<String?>) {
        photoPrefs().edit().putString(uid, List(3) { slots.getOrNull(it).orEmpty() }.joinToString("\n")).apply()
    }

    private fun encodePhotos(photos: List<ProfilePhoto?>): List<Map<String, String>> {
        return photos.mapNotNull { photo ->
            photo?.takeIf { it.id.isNotBlank() && it.url.startsWith("https://") }?.let {
                mapOf(
                    "id" to it.id,
                    "url" to it.url,
                    "status" to it.status.wire,
                )
            }
        }
    }

    private fun decodePhotos(raw: Any?): List<ProfilePhoto?> {
        val parsed = (raw as? List<*>)?.mapNotNull { item ->
            val map = item as? Map<*, *> ?: return@mapNotNull null
            val id = map["id"] as? String ?: return@mapNotNull null
            val url = map["url"] as? String ?: return@mapNotNull null
            if (id.isBlank() || !url.startsWith("https://")) return@mapNotNull null
            ProfilePhoto(id, url, PhotoStatus.fromWire(map["status"] as? String))
        }.orEmpty()
        return List(3) { index -> parsed.getOrNull(index) }
    }

    private fun installAppCheck() {
        if (appCheckInstalled) return
        com.google.firebase.appcheck.FirebaseAppCheck.getInstance()
            .installAppCheckProviderFactory(appCheckFactory())
        appCheckInstalled = true
    }

    private suspend fun registerThisDevice() {
        val deviceId = android.provider.Settings.Secure.getString(
            appContext.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID,
        )?.trim()?.lowercase().orEmpty()
        if (!deviceId.matches(Regex("^[0-9a-f]{8,64}$"))) {
            error("This phone couldn't be checked. Try again.")
        }
        functions.getHttpsCallable("registerDevice").call(hashMapOf("deviceId" to deviceId)).await()
    }

    suspend fun ensureDeviceRegistered() {
        if (!ready() || auth.currentUser == null) return
        try {
            registerThisDevice()
        } catch (_: Exception) {
            // The next launch tries again. Messages stay locked until the device is stored.
        }
    }

    private fun connectEmulators() {
        if (!USE_EMULATORS || emulatorsConnected) return
        emulatorsConnected = true
        FirebaseAuth.getInstance().useEmulator(EMULATOR_HOST, 9099)
        FirebaseFirestore.getInstance().useEmulator(EMULATOR_HOST, 8080)
        FirebaseFunctions.getInstance("asia-south1").useEmulator(EMULATOR_HOST, 5001)
        runCatching { com.google.firebase.database.FirebaseDatabase.getInstance().useEmulator(EMULATOR_HOST, 9000) }
    }

    companion object {
        const val STARTING_COINS = 50
        private const val USE_EMULATORS = false
        private const val EMULATOR_HOST = "10.0.2.2"
        private var emulatorsConnected = false
        private var appCheckInstalled = false
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
