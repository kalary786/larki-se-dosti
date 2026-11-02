package com.larkisedosti.app.model

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.ListenerRegistration
import com.larkisedosti.app.data.CallChargeResult
import com.larkisedosti.app.data.FirebaseAccount
import com.larkisedosti.app.data.GiftCallResult
import com.larkisedosti.app.data.GoogleAuthResult
import com.larkisedosti.app.data.MessageCallResult
import com.larkisedosti.app.data.LinkGoogleResult
import com.larkisedosti.app.BuildConfig
import com.larkisedosti.app.data.PresenceState
import com.larkisedosti.app.data.UserProfile
import com.larkisedosti.app.data.UsernameTakenException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Calendar

class AppStore(
    private val account: FirebaseAccount,
) {
    private val writes = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    var signedIn by mutableStateOf(false)
    var userId by mutableStateOf("")
    var username by mutableStateOf("")
    var shortId by mutableLongStateOf(0L)
    var isGoogleLinked by mutableStateOf(false)
    var name by mutableStateOf("")
    var dateOfBirth by mutableStateOf("")
    var createdAt by mutableStateOf(0L)
    var age by mutableIntStateOf(21)
    var gender by mutableStateOf(Gender.Female)
    var country by mutableStateOf(Countries.first())
    val photos = mutableStateListOf<ProfilePhoto?>(null, null, null)
    val photoProgress = mutableStateListOf<Float?>(null, null, null)
    var photoMessage by mutableStateOf<String?>(null)
    val reports = mutableStateListOf<UserReport>()
    var coins by mutableIntStateOf(0)
    var freeMessagesUsed by mutableIntStateOf(0)
    var freeMessageQuota by mutableIntStateOf(0)
    var messageCost by mutableIntStateOf(0)
    var rewardAdFreeMessages by mutableIntStateOf(0)
    var messageAllowanceKnown by mutableStateOf(false)
    var giftsSent by mutableIntStateOf(0)
    val giftCatalog = mutableStateListOf<CatalogGift>()
    var boosted by mutableStateOf(false)
    var notificationsOn by mutableStateOf(true)
    var chatSounds by mutableStateOf(true)
    var level by mutableIntStateOf(1)

    val favorites = mutableStateListOf<String>()
    val friendIds = mutableStateListOf<String>()
    val requestIds = mutableStateListOf<String>()
    val blockedIds = mutableStateListOf<String>()
    val directory = mutableStateListOf<Person>()
    val discoverPeople = mutableStateListOf<Person>()
    val blockedByIds = mutableStateListOf<String>()
    var discoverLoadingMore by mutableStateOf(false)
        private set
    val conversations = mutableStateListOf<Conversation>()
    val notices = mutableStateListOf<Notice>()

    private var sequence = 0
    private var statsListener: ListenerRegistration? = null
    private var giftListener: ListenerRegistration? = null
    private var blockListener: ListenerRegistration? = null
    private var blockedByListener: ListenerRegistration? = null
    private var discoverGender: String? = null
    private var discoverCountry: String? = null
    private var discoverHasMore = true
    private val discoverLock = Mutex()
    private val presence = mutableStateMapOf<String, PresenceState>()
    private var settingsListener: ListenerRegistration? = null
    private var settingsKnown = false
    private var usageKnown = false
    var discoverError by mutableStateOf<String?>(null)

    val unreadChats: Int get() = conversations.sumOf { it.unread }
    val freeMessagesLeft: Int get() = (freeMessageQuota - freeMessagesUsed).coerceAtLeast(0)
    val unreadNotices: Int get() = notices.count { !it.read }

    init {
        account.bindPresence(
            onUpdate = { uid, online, at -> presence[uid] = PresenceState(online, at) },
            onClear = { uid -> presence.remove(uid) },
        )
        directory.addAll(samplePeople())
        friendIds.addAll(listOf("p1", "p2"))
        requestIds.addAll(listOf("p3", "p4"))
        conversations.add(thread("p1", 0, "Yesterday", listOf(
            ChatMessage("a1", false, "You around this evening?", "8:14 PM", sentAt = clockAt(1, 20, 14)),
            ChatMessage("a2", true, "I am. Want to talk for a bit?", "8:21 PM", sentAt = clockAt(1, 20, 21)),
        )))
        conversations.add(thread("p5", 2, "9:40 PM", listOf(
            ChatMessage("b1", false, "That city view you mentioned sounds nice.", "9:12 AM", sentAt = clockAt(0, 9, 12)),
            ChatMessage("b2", false, "Are you free later?", "9:40 AM", sentAt = clockAt(0, 9, 40)),
        )))
        conversations.add(thread("p6", 1, "Oct 20", listOf(
            ChatMessage("c1", false, "Sent you something small.", "8:02 AM", GiftKind.Rose, sentAt = clockAt(2, 8, 2)),
            ChatMessage("c2", false, "Hope it makes you smile.", "8:05 AM", sentAt = clockAt(2, 8, 5)),
        )))
        notices.add(Notice("n1", "Friend request", "Sana wants to be friends.", "2h", "request", "p3"))
        notices.add(Notice("n2", "New message", "Maya sent you a message.", "1h", "message", "p5"))
        notices.add(Notice("n3", "Gift", "Noor sent you a rose.", "35m", "gift", "p6"))
        notices.add(Notice("n4", "Incoming call", "Hania is calling.", "12m", "call", "p2"))
    }

    fun person(id: String): Person? = discoverPeople.find { it.id == id } ?: directory.find { it.id == id }

    fun live(person: Person): Person {
        val state = presence[person.id] ?: return person
        val seen = if (state.lastChanged > 0L) state.lastChanged else person.lastSeen
        if (state.online == person.online && seen == person.lastSeen) return person
        return person.copy(online = state.online, lastSeen = seen)
    }

    fun watchPresence(scope: String, ids: Collection<String>) {
        account.watchPresence(scope, ids)
    }

    fun toggleFavorite(id: String) {
        if (id in favorites) favorites.remove(id) else favorites.add(id)
    }

    fun addFriend(id: String) {
        requestIds.remove(id)
        if (id !in friendIds) friendIds.add(id)
    }

    fun acceptRequest(id: String) = addFriend(id)

    fun declineRequest(id: String) {
        requestIds.remove(id)
    }

    fun block(id: String) {
        if (id !in blockedIds) blockedIds.add(id)
        friendIds.remove(id)
        requestIds.remove(id)
        favorites.remove(id)
        conversations.removeAll { it.personId == id }
        account.saveBlock(userId, id, blocked = true)
    }

    fun unblock(id: String) {
        blockedIds.remove(id)
        account.saveBlock(userId, id, blocked = false)
    }

    fun fileReport(personId: String, reason: String, note: String) {
        if (reason.isBlank()) return
        reports.add(UserReport(personId, reason, note))
    }

    fun uploadPhoto(index: Int, source: Uri) {
        if (index !in photos.indices || userId.isBlank() || photoProgress[index] != null) return
        photoProgress[index] = 0.15f
        writes.launch {
            try {
                photoMessage = null
                val saved = withContext(Dispatchers.IO) { account.saveLocalPhoto(userId, index, source) }
                photos[index] = saved
            } catch (_: Exception) {
                photoMessage = "Couldn't save that photo on this phone."
            } finally {
                photoProgress[index] = null
            }
        }
    }

    fun deletePhoto(index: Int) {
        if (photos.getOrNull(index) == null || photoProgress[index] != null || userId.isBlank()) return
        account.removeLocalPhoto(userId, index)
        photos[index] = null
        photoMessage = null
    }

    suspend fun reloadDiscover(gender: String?, country: String?) {
        if (!signedIn || userId.isBlank()) return
        discoverLock.withLock {
            val changed = gender != discoverGender || country != discoverCountry
            discoverGender = gender
            discoverCountry = country
            if (changed) discoverPeople.clear()
            discoverHasMore = true
            discoverError = null
            try {
                val next = mutableListOf<Person>()
                collectDiscover(next, reset = true)
                discoverPeople.clear()
                discoverPeople.addAll(next)
            } catch (error: Exception) {
                discoverError = discoverFailure(error)
            }
        }
    }

    suspend fun loadMoreDiscover() {
        if (!signedIn || userId.isBlank() || !discoverHasMore || discoverLoadingMore) return
        discoverLock.withLock {
            if (!discoverHasMore) return@withLock
            discoverLoadingMore = true
            try {
                val next = mutableListOf<Person>()
                collectDiscover(next, reset = false)
                val seen = discoverPeople.map { it.id }.toSet()
                discoverPeople.addAll(next.filter { it.id !in seen })
            } catch (error: Exception) {
                discoverError = discoverFailure(error)
            } finally {
                discoverLoadingMore = false
            }
        }
    }

    suspend fun seedTestUsers() {
        account.seedTestUsers()
        reloadDiscover(discoverGender, discoverCountry)
    }

    suspend fun deleteTestUsers() {
        account.deleteTestUsers()
        reloadDiscover(discoverGender, discoverCountry)
    }

    private suspend fun collectDiscover(into: MutableList<Person>, reset: Boolean) {
        var resetCursor = reset
        var rounds = 0
        while (into.size < 20 && discoverHasMore && rounds < 6) {
            val page = account.fetchDiscoverPage(discoverGender, discoverCountry, resetCursor)
            resetCursor = false
            discoverHasMore = page.hasMore
            page.profiles.forEach { profile ->
                if (!acceptDiscover(profile)) return@forEach
                if (into.any { it.id == profile.uid }) return@forEach
                into += profile.toDiscoverPerson()
            }
            rounds += 1
        }
    }

    private fun acceptDiscover(profile: UserProfile): Boolean {
        if (profile.uid == userId) return false
        if (profile.uid in blockedIds || profile.uid in blockedByIds) return false
        if (!BuildConfig.DEBUG && profile.isTestUser) return false
        return true
    }

    private fun discoverFailure(error: Exception): String {
        val detail = error.message.orEmpty()
        return if (detail.contains("http", ignoreCase = true)) detail
        else "Couldn't load people. Check your connection and pull to try again."
    }

    private fun hideBlockedPeople() {
        val hidden = blockedIds.toSet() + blockedByIds.toSet()
        if (hidden.isEmpty()) return
        discoverPeople.removeAll { it.id in hidden }
    }

    fun restore(profile: UserProfile) {
        userId = profile.uid
        username = profile.username
        shortId = profile.shortId
        isGoogleLinked = profile.isGoogleLinked
        name = profile.displayName
        dateOfBirth = profile.dateOfBirth
        age = ageFromDate(profile.dateOfBirth)
        gender = profile.gender
        country = profile.country
        createdAt = profile.createdAt
        coins = profile.coins
        level = profile.level
        watchAccount(profile.uid)
        writes.launch { account.ensureDeviceRegistered() }
        val local = account.localPhotos(profile.uid)
        repeat(3) { index -> photos[index] = local.getOrNull(index) }
        signedIn = profile.onboardingCompleted
    }

    suspend fun completeOnboarding(
        username: String,
        name: String,
        dateOfBirth: String,
        gender: Gender,
        country: Country,
        photo: String?,
    ): String? {
        return try {
            val profile = account.createProfile(
                username = username,
                displayName = name,
                dateOfBirth = dateOfBirth,
                gender = gender,
                country = country,
                photos = listOf(null, null, null),
            )
            restore(profile)
            if (!photo.isNullOrBlank()) {
                try {
                    photos[0] = withContext(Dispatchers.IO) {
                        account.saveLocalPhoto(profile.uid, 0, Uri.parse(photo))
                    }
                } catch (_: Exception) {
                    photoMessage = "Couldn't save that photo on this phone."
                }
            }
            null
        } catch (_: UsernameTakenException) {
            "That username was just taken. Try another."
        } catch (_: Exception) {
            "Couldn't save your profile. Check your connection."
        }
    }

    suspend fun updateProfile(name: String, gender: Gender, country: Country): String? {
        val clean = name.trim().ifBlank { this.name }
        return try {
            account.updateProfile(clean, gender, country)
            this.name = clean
            this.gender = gender
            this.country = country
            null
        } catch (_: Exception) {
            "Couldn't save those changes."
        }
    }

    suspend fun usernameAvailable(username: String): Boolean = account.isUsernameAvailable(username)

    suspend fun signInWithGoogle(activity: Activity): GoogleAuthResult {
        val result = account.signInWithGoogle(activity)
        if (result is GoogleAuthResult.Ready && result.profile != null) restore(result.profile)
        return result
    }

    suspend fun linkGoogle(activity: Activity): LinkGoogleResult {
        val result = account.linkGoogle(activity)
        if (result is LinkGoogleResult.Linked) isGoogleLinked = true
        return result
    }

    suspend fun useExistingGoogleAccount(): UserProfile? {
        val profile = account.signIntoLinkedGoogle()
        if (profile != null) restore(profile) else clearLocal()
        return profile
    }

    fun photoVisibleToOthers(index: Int): String? {
        val photo = photos.getOrNull(index) ?: return null
        return photo.url.takeIf { photo.status == PhotoStatus.Approved }
    }

    fun openConversation(personId: String): Conversation {
        val existing = conversations.find { it.personId == personId }
        if (existing != null) return existing
        val created = Conversation(personId, 0, "Now")
        conversations.add(0, created)
        return created
    }

    fun markRead(personId: String) {
        conversations.find { it.personId == personId }?.unread = 0
    }

    fun showOutgoing(personId: String, messageId: String, text: String) {
        val conversation = openConversation(personId)
        val index = conversation.messages.indexOfFirst { it.id == messageId }
        if (index >= 0) {
            val current = conversation.messages[index]
            conversation.messages[index] = current.copy(delivery = MessageDelivery.Sending)
        } else {
            conversation.messages.add(
                ChatMessage(messageId, true, text, clockNow(), delivery = MessageDelivery.Sending),
            )
            conversation.timeLabel = clockNow()
            bump(conversation)
        }
    }

    fun newMessageId(): String = java.util.UUID.randomUUID().toString().replace("-", "")

    suspend fun sendText(personId: String, messageId: String, text: String): TextSendResult {
        val clean = text.trim()
        if (clean.isEmpty() || clean.length > 1000) {
            markDelivery(personId, messageId, MessageDelivery.Failed)
            return TextSendResult.Failed("Write a message first.")
        }
        if (userId.isBlank() || personId == userId) {
            markDelivery(personId, messageId, MessageDelivery.Failed)
            return TextSendResult.Failed("You can't message yourself.")
        }
        val chatId = account.chatIdFor(userId, personId)
        return when (val result = account.sendMessage(chatId, personId, clean, messageId)) {
            is MessageCallResult.Sent -> {
                coins = result.coins
                if (freeMessageQuota > 0) {
                    freeMessagesUsed = (freeMessageQuota - result.freeMessagesLeft).coerceAtLeast(0)
                }
                markDelivery(personId, messageId, MessageDelivery.Sent)
                TextSendResult.Sent
            }
            MessageCallResult.NoFreeMessages -> {
                markDelivery(personId, messageId, MessageDelivery.Failed)
                TextSendResult.NoFreeMessages
            }
            is MessageCallResult.Failed -> {
                markDelivery(personId, messageId, MessageDelivery.Failed)
                TextSendResult.Failed(result.message)
            }
        }
    }

    private fun markDelivery(personId: String, messageId: String, delivery: MessageDelivery) {
        val conversation = conversations.find { it.personId == personId } ?: return
        val index = conversation.messages.indexOfFirst { it.id == messageId }
        if (index >= 0) {
            conversation.messages[index] = conversation.messages[index].copy(delivery = delivery)
        }
    }

    suspend fun sendGift(personId: String, gift: CatalogGift): GiftSendResult {
        if (userId.isBlank() || personId == userId) {
            return GiftSendResult.Failed("You can't send a gift to yourself.")
        }
        val chatId = account.chatIdFor(userId, personId)
        return when (val result = account.sendGift(personId, gift.id, chatId)) {
            is GiftCallResult.Sent -> {
                coins = result.balance
                val kind = GiftKind.entries.find { it.name.equals(gift.id, ignoreCase = true) }
                val conversation = openConversation(personId)
                conversation.messages.add(
                    ChatMessage(nextId(), true, gift.name, clockNow(), kind),
                )
                conversation.timeLabel = clockNow()
                bump(conversation)
                GiftSendResult.Sent(gift.name)
            }
            GiftCallResult.Insufficient -> GiftSendResult.Insufficient
            is GiftCallResult.Failed -> GiftSendResult.Failed(result.message)
        }
    }

    suspend fun ensureGiftCatalog() {
        account.ensureGiftCatalog()
    }

    fun listenLedger(onUpdate: (List<LedgerEntry>) -> Unit): ListenerRegistration? {
        return account.listenLedger(userId, onUpdate)
    }

    fun canAffordCall(): Boolean = coins >= CallCost

    suspend fun chargeCall(otherUid: String): Boolean {
        if (discoverPeople.none { it.id == otherUid }) return coins >= CallCost
        return when (val result = account.chargeCall(otherUid)) {
            is CallChargeResult.Charged -> {
                coins = result.balance
                true
            }
            CallChargeResult.Insufficient -> false
            CallChargeResult.Failed -> false
        }
    }

    suspend fun deleteAccount() {
        account.deleteLocalPhotos(userId)
        account.deleteAccount()
        clearLocal()
    }

    suspend fun logOut() {
        account.logOut()
        clearLocal()
    }

    private fun clearLocal() {
        signedIn = false
        userId = ""
        username = ""
        shortId = 0L
        isGoogleLinked = false
        name = ""
        dateOfBirth = ""
        createdAt = 0L
        age = 21
        gender = Gender.Female
        country = Countries.first()
        coins = 0
        freeMessagesUsed = 0
        freeMessageQuota = 0
        messageCost = 0
        rewardAdFreeMessages = 0
        messageAllowanceKnown = false
        settingsKnown = false
        usageKnown = false
        level = 1
        photos[0] = null
        photos[1] = null
        photos[2] = null
        photoProgress[0] = null
        photoProgress[1] = null
        photoProgress[2] = null
        photoMessage = null
        discoverPeople.clear()
        discoverError = null
        reports.clear()
        conversations.clear()
        notices.clear()
        friendIds.clear()
        requestIds.clear()
        favorites.clear()
        blockedIds.clear()
        blockedByIds.clear()
        giftsSent = 0
        giftCatalog.clear()
        statsListener?.remove()
        giftListener?.remove()
        blockListener?.remove()
        blockedByListener?.remove()
        settingsListener?.remove()
        statsListener = null
        giftListener = null
        blockListener = null
        blockedByListener = null
        settingsListener = null
        boosted = false
    }

    private fun watchAccount(uid: String) {
        statsListener?.remove()
        giftListener?.remove()
        blockListener?.remove()
        blockedByListener?.remove()
        settingsListener?.remove()
        statsListener = account.listenStats(uid) { nextCoins, sent, used ->
            coins = nextCoins
            if (sent != null) giftsSent = sent
            freeMessagesUsed = used
            usageKnown = true
            messageAllowanceKnown = settingsKnown
        }
        settingsListener = account.listenAppSettings { free, cost, reward ->
            freeMessageQuota = free
            messageCost = cost
            rewardAdFreeMessages = reward
            settingsKnown = true
            messageAllowanceKnown = usageKnown
        }
        giftListener = account.listenGifts { next ->
            giftCatalog.clear()
            giftCatalog.addAll(next)
        }
        blockListener = account.listenBlocks(uid) { ids ->
            blockedIds.clear()
            blockedIds.addAll(ids)
            hideBlockedPeople()
        }
        blockedByListener = account.listenBlockedBy(uid) { ids ->
            blockedByIds.clear()
            blockedByIds.addAll(ids)
            hideBlockedPeople()
        }
    }

    private fun bump(conversation: Conversation) {
        conversations.remove(conversation)
        conversations.add(0, conversation)
    }

    private fun nextId(): String {
        sequence += 1
        return "local-$sequence"
    }

    private fun thread(
        personId: String,
        unread: Int,
        time: String,
        seed: List<ChatMessage>,
    ): Conversation {
        return Conversation(personId, unread, time).also { it.messages.addAll(seed) }
    }

    companion object {
        const val CallCost = 10
        const val StartingCoins = FirebaseAccount.STARTING_COINS
    }
}

private fun UserProfile.toDiscoverPerson(): Person {
    val approved = photos.mapNotNull { photo ->
        photo?.takeIf { it.status == PhotoStatus.Approved && it.url.startsWith("https://") }?.url
    }
    val bucket = uid.hashCode().and(Int.MAX_VALUE)
    return Person(
        id = uid,
        name = displayName,
        age = ageFromDate(dateOfBirth),
        gender = gender,
        city = country.city,
        region = country.region,
        countryName = country.name,
        countryFlag = country.flag,
        online = isOnline,
        bio = "",
        verified = false,
        ring = bucket % 4,
        bearing = (bucket % 360).toFloat(),
        color = portraitColors[bucket % portraitColors.size],
        panels = approved.size.coerceAtLeast(1),
        photoUrl = approved.firstOrNull(),
        photoUrls = approved,
        username = username,
        lastSeen = lastSeen,
    )
}

internal fun presenceLabel(online: Boolean, lastSeen: Long, now: Long = System.currentTimeMillis()): String {
    if (online) return "Online"
    if (lastSeen <= 0L) return "Offline"
    val minutes = ((now - lastSeen) / 60_000L).coerceAtLeast(0L)
    if (minutes < 1L) return "Active just now"
    if (minutes < 60L) return "Active ${minutes}m ago"
    if (minutes < 60L * 24L) return "Active ${minutes / 60L}h ago"
    val seen = Calendar.getInstance().apply { timeInMillis = lastSeen }
    val startOfToday = Calendar.getInstance().apply {
        timeInMillis = now
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val startOfYesterday = (startOfToday.clone() as Calendar).apply { add(Calendar.DATE, -1) }
    if (!seen.before(startOfYesterday) && seen.before(startOfToday)) return "Active yesterday"
    return "Active ${(minutes / (60L * 24L)).coerceAtLeast(2L)}d ago"
}

private val portraitColors = listOf(
    Color(0xFF3A4A6B),
    Color(0xFF5C4A3A),
    Color(0xFF2F4F4A),
    Color(0xFF4A3B5C),
    Color(0xFF3E3A55),
    Color(0xFF5A3E45),
    Color(0xFF3D4E3A),
    Color(0xFF4E4450),
)

private fun samplePeople(): List<Person> {
    fun person(
        id: String,
        name: String,
        age: Int,
        gender: Gender,
        country: Country,
        city: String = country.city,
        region: String = country.region,
        online: Boolean,
        bio: String,
        verified: Boolean,
        ring: Int,
        bearing: Float,
        color: Color,
        panels: Int,
        photoUrl: String? = null,
    ) = Person(
        id, name, age, gender, city, region, country.name, country.flag,
        online, bio, verified, ring, bearing, color, panels, photoUrl,
    )

    val pk = Countries[0]
    val uae = Countries[3]
    val us = Countries[6]
    val uk = Countries[5]
    val tr = Countries[8]
    val bd = Countries[2]
    val ca = Countries[7]
    val eg = Countries[11]
    val my = Countries[9]
    val sa = Countries[4]
    return listOf(
        person("p1", "Areeba", 24, Gender.Female, pk, online = true, bio = "Coffee, long walks, and voice notes after dark.", verified = true, ring = 1, bearing = 30f, color = portraitColors[0], panels = 3, photoUrl = "https://i.pravatar.cc/400?img=47"),
        person("p2", "Hania", 23, Gender.Female, pk, "Karachi", "Sindh", online = true, bio = "I talk more once the small talk is over.", verified = false, ring = 0, bearing = 140f, color = portraitColors[1], panels = 2, photoUrl = "https://i.pravatar.cc/400?img=45"),
        person("p3", "Sana", 26, Gender.Female, uae, online = false, bio = "New in the city. Looking for an easy conversation.", verified = true, ring = 3, bearing = 200f, color = portraitColors[2], panels = 2, photoUrl = "https://i.pravatar.cc/400?img=44"),
        person("p4", "Omar", 27, Gender.Male, pk, "Islamabad", "Capital", online = true, bio = "Books, night drives, and bad puns.", verified = false, ring = 2, bearing = 250f, color = portraitColors[3], panels = 1, photoUrl = "https://i.pravatar.cc/400?img=12"),
        person("p5", "Maya", 22, Gender.Female, uk, online = true, bio = "Tell me what you are listening to.", verified = true, ring = 3, bearing = 80f, color = portraitColors[4], panels = 3, photoUrl = "https://i.pravatar.cc/400?img=48"),
        person("p6", "Noor", 24, Gender.Female, bd, online = true, bio = "Soft hours only. I like slow replies.", verified = false, ring = 3, bearing = 310f, color = portraitColors[5], panels = 2, photoUrl = "https://i.pravatar.cc/400?img=49"),
        person("p7", "Leo", 25, Gender.Male, us, online = true, bio = "Here between shifts. Say hello if you are up.", verified = false, ring = 3, bearing = 20f, color = portraitColors[6], panels = 1, photoUrl = "https://i.pravatar.cc/400?img=15"),
        person("p8", "Yusuf", 29, Gender.Male, tr, online = false, bio = "Cooking, cities, and one good question.", verified = true, ring = 2, bearing = 170f, color = portraitColors[7], panels = 2, photoUrl = "https://i.pravatar.cc/400?img=13"),
        person("p9", "Adam", 28, Gender.Male, ca, online = true, bio = "I will match your energy, then raise it a little.", verified = false, ring = 1, bearing = 330f, color = portraitColors[0], panels = 1, photoUrl = "https://i.pravatar.cc/400?img=14"),
        person("p10", "Lina", 23, Gender.Female, eg, online = false, bio = "Film stills and late breakfasts.", verified = false, ring = 0, bearing = 100f, color = portraitColors[2], panels = 3, photoUrl = "https://i.pravatar.cc/400?img=32"),
        person("p11", "Zara", 21, Gender.Female, my, online = true, bio = "Friends first. We can see where it goes.", verified = true, ring = 3, bearing = 50f, color = portraitColors[3], panels = 2, photoUrl = "https://i.pravatar.cc/400?img=25"),
        person("p12", "Bilal", 30, Gender.Male, sa, online = true, bio = "Quiet, direct, and usually online after ten.", verified = false, ring = 3, bearing = 220f, color = portraitColors[1], panels = 1, photoUrl = "https://i.pravatar.cc/400?img=11"),
    )
}

internal fun ageFromDate(dateOfBirth: String, today: Calendar = Calendar.getInstance()): Int {
    val parts = dateOfBirth.split("-")
    val year = parts[0].toInt()
    val month = parts[1].toInt()
    val day = parts[2].toInt()
    var years = today.get(Calendar.YEAR) - year
    val currentMonth = today.get(Calendar.MONTH) + 1
    val currentDay = today.get(Calendar.DAY_OF_MONTH)
    if (currentMonth < month || (currentMonth == month && currentDay < day)) years -= 1
    return years
}

internal fun clockNow(): String {
    return formatClock(System.currentTimeMillis())
}

internal fun formatClock(millis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    val hour = calendar.get(Calendar.HOUR).let { if (it == 0) 12 else it }
    val minute = calendar.get(Calendar.MINUTE)
    val suffix = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
    return "%d:%02d %s".format(hour, minute, suffix)
}

internal fun formatChatDay(millis: Long): String {
    val today = Calendar.getInstance()
    val then = Calendar.getInstance().apply { timeInMillis = millis }
    val sameYear = today.get(Calendar.YEAR) == then.get(Calendar.YEAR)
    val dayDelta = today.get(Calendar.DAY_OF_YEAR) - then.get(Calendar.DAY_OF_YEAR)
    if (sameYear && dayDelta == 0) return "Today"
    if (sameYear && dayDelta == 1) return "Yesterday"
    val month = then.getDisplayName(Calendar.MONTH, Calendar.SHORT, java.util.Locale.US)
    return "$month ${then.get(Calendar.DAY_OF_MONTH)}"
}

private fun clockAt(daysAgo: Int, hour24: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -daysAgo)
    calendar.set(Calendar.HOUR_OF_DAY, hour24)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
