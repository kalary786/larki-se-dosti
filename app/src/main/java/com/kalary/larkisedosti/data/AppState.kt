package com.kalary.larkisedosti.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel

enum class MainTab { Discover, Match, Chats, Friends, Profile }

enum class FriendsSegment { Friends, Requests }

enum class DiscoverFilter { All, Girls, Boys, Country }

enum class ChatFilter { All, Unread, Online }

enum class MatchFilter { All, Girls, Boys }

enum class VideoReward { Coins, FreeAction }

sealed class GateAction {
    data class Call(val personId: String, val video: Boolean, val incoming: Boolean) : GateAction()
    data class GiftSend(val personId: String, val giftId: String) : GateAction()
}

data class CoinGate(
    val need: Int,
    val pill: String,
    val action: GateAction,
)

class AppState {
    var darkTheme by mutableStateOf(true)
    var coins by mutableIntStateOf(MockData.STARTING_COINS)
    var isVip by mutableStateOf(false)
    var notificationsOn by mutableStateOf(true)
    var chatSounds by mutableStateOf(true)
    var onboarded by mutableStateOf(false)
    var onboardingStep by mutableIntStateOf(0)
    var ageText by mutableStateOf("")
    var draftName by mutableStateOf("")
    var draftGender by mutableStateOf<Gender?>(null)
    var draftCountry by mutableStateOf<Country?>(null)
    var draftPhoto by mutableStateOf(false)
    var termsAccepted by mutableStateOf(false)

    var myName by mutableStateOf("")
    var myAge by mutableIntStateOf(0)
    var myGender by mutableStateOf(Gender.Female)
    var myCountry by mutableStateOf(MockData.countries.first())
    var photoFilled by mutableStateOf(listOf(false, false, false))

    var selectedTab by mutableStateOf(MainTab.Discover)
    var friendsSegment by mutableStateOf(FriendsSegment.Friends)
    var discoverFilter by mutableStateOf(DiscoverFilter.All)
    var discoverCountry by mutableStateOf<Country?>(null)
    var matchFilter by mutableStateOf(MatchFilter.All)
    var radarSound by mutableStateOf(true)
    var chatFilter by mutableStateOf(ChatFilter.All)
    var chatQuery by mutableStateOf("")

    val people = mutableStateListOf<Person>().apply { addAll(MockData.people) }
    val favoriteIds = mutableStateListOf<String>()
    val friendIds = mutableStateListOf<String>().apply { addAll(MockData.friendIds) }
    val requestIds = mutableStateListOf<String>().apply { addAll(MockData.requestIds) }
    val blockedIds = mutableStateListOf<String>()
    val conversations = mutableStateListOf<Conversation>().apply { addAll(MockData.conversations()) }
    val notifications = mutableStateListOf<AppNotification>().apply { addAll(MockData.notifications()) }

    var discoverExtras by mutableIntStateOf(0)
    var discoverLoading by mutableStateOf(true)
    var discoverRefreshing by mutableStateOf(false)
    var loadingMore by mutableStateOf(false)

    var sessionCount by mutableIntStateOf(0)
    var rateDismissedAt by mutableLongStateOf(0L)
    var rateVisible by mutableStateOf(false)
    var rateChecked by mutableStateOf(false)

    var snackbar by mutableStateOf<String?>(null)
    var gate by mutableStateOf<CoinGate?>(null)
    var burstGiftId by mutableStateOf<String?>(null)
    var videoReward by mutableStateOf<VideoReward?>(null)
    var freePass by mutableStateOf(false)
    var reportFor by mutableStateOf<String?>(null)
    var blockFor by mutableStateOf<String?>(null)
    var deleteChatFor by mutableStateOf<String?>(null)
    var confirmDeleteAccount by mutableStateOf(false)
    var typingFor by mutableStateOf<String?>(null)
    var giftsReceived by mutableIntStateOf(MockData.GIFTS_RECEIVED)

    private var messageSeq = 100

    val userId: String = MockData.USER_ID
    val level: Int = MockData.LEVEL

    val unreadChats: Int
        get() = conversations.filter { !isBlocked(it.personId) }.sumOf { it.unread }

    val unreadNotes: Int
        get() = if (notificationsOn) notifications.count { it.unread } else 0

    fun baseId(id: String) = id.substringBefore("#")

    fun person(id: String): Person? {
        val base = baseId(id)
        return people.find { it.id == base } ?: MockData.people.find { it.id == base }
    }

    fun isBlocked(id: String) = baseId(id) in blockedIds

    fun isFavorite(id: String) = baseId(id) in favoriteIds

    fun isFriend(id: String) = baseId(id) in friendIds

    fun visiblePeople(filter: DiscoverFilter = discoverFilter, country: Country? = discoverCountry): List<Person> {
        return people.filter { person ->
            if (isBlocked(person.id)) return@filter false
            when (filter) {
                DiscoverFilter.All -> true
                DiscoverFilter.Girls -> person.gender == Gender.Female
                DiscoverFilter.Boys -> person.gender == Gender.Male
                DiscoverFilter.Country -> country != null && person.country == country.name
            }
        }
    }

    fun matchPeople(): List<Person> {
        return people.filter { person ->
            if ("#" in person.id || isBlocked(person.id)) return@filter false
            when (matchFilter) {
                MatchFilter.All -> true
                MatchFilter.Girls -> person.gender == Gender.Female
                MatchFilter.Boys -> person.gender == Gender.Male
            }
        }
    }

    fun visibleChats(): List<Conversation> {
        val query = chatQuery.trim().lowercase()
        return conversations.filter { chat ->
            val person = person(chat.personId) ?: return@filter false
            if (isBlocked(person.id)) return@filter false
            val matchesQuery = query.isEmpty() ||
                person.name.lowercase().contains(query) ||
                chat.preview.lowercase().contains(query)
            val matchesFilter = when (chatFilter) {
                ChatFilter.All -> true
                ChatFilter.Unread -> chat.unread > 0
                ChatFilter.Online -> person.online
            }
            matchesQuery && matchesFilter
        }
    }

    fun conversation(personId: String): Conversation? =
        conversations.firstOrNull { it.personId == baseId(personId) }

    fun toggleFavorite(id: String) {
        val base = baseId(id)
        if (base in favoriteIds) favoriteIds.remove(base) else favoriteIds.add(base)
    }

    fun addFriend(id: String) {
        val base = baseId(id)
        if (base in friendIds) {
            snackbar = "You're already friends"
            return
        }
        requestIds.remove(base)
        friendIds.add(base)
        val name = person(base)?.name ?: "them"
        snackbar = "You and $name are friends now"
    }

    fun acceptRequest(id: String) {
        requestIds.remove(id)
        if (id !in friendIds) friendIds.add(id)
        snackbar = "Request accepted"
    }

    fun declineRequest(id: String) {
        requestIds.remove(id)
        snackbar = "Request declined"
    }

    fun block(id: String) {
        val base = baseId(id)
        if (base !in blockedIds) blockedIds.add(base)
        friendIds.remove(base)
        requestIds.remove(base)
        favoriteIds.remove(base)
        conversations.removeAll { it.personId == base }
        val name = person(base)?.name ?: "this person"
        snackbar = "$name is blocked"
    }

    fun unblock(id: String) {
        blockedIds.remove(baseId(id))
        snackbar = "Unblocked"
    }

    fun deleteChat(id: String) {
        conversations.removeAll { it.personId == baseId(id) }
        snackbar = "Chat deleted"
    }

    fun markChatRead(personId: String) {
        conversation(personId)?.unread = 0
    }

    fun markNotificationRead(id: String) {
        val index = notifications.indexOfFirst { it.id == id }
        if (index >= 0) notifications[index] = notifications[index].copy(unread = false)
    }

    fun ensureChat(personId: String): Conversation {
        val base = baseId(personId)
        conversation(base)?.let { return it }
        val created = Conversation(base, emptyList(), 0)
        conversations.add(0, created)
        return created
    }

    fun sendText(personId: String, text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty() || isBlocked(personId)) return
        val chat = ensureChat(personId)
        val kind = if (trimmed.none { it.isLetterOrDigit() }) MessageKind.Emoji else MessageKind.Text
        chat.messages.add(ChatMessage(nextMessageId(), true, kind, trimmed, "Now"))
        chat.unread = 0
    }

    fun deliverGift(personId: String, gift: Gift) {
        if (isBlocked(personId)) return
        val chat = ensureChat(personId)
        chat.messages.add(
            ChatMessage(nextMessageId(), true, MessageKind.Gift, gift.name, "Now", gift.id),
        )
        burstGiftId = gift.id
        snackbar = "Sent ${gift.name}"
    }

    fun reply(personId: String) {
        if (isBlocked(personId)) return
        val chat = ensureChat(personId)
        val line = MockData.replies.random()
        chat.messages.add(ChatMessage(nextMessageId(), false, MessageKind.Text, line, "Now"))
    }

    fun spend(amount: Int): Boolean {
        if (coins < amount) return false
        coins -= amount
        return true
    }

    fun buy(pack: CoinPack) {
        coins += pack.coins
        snackbar = "${pack.coins} coins added"
    }

    fun grantVideoCoins() {
        coins += MockData.VIDEO_COINS
        snackbar = "${MockData.VIDEO_COINS} free coins added"
    }

    fun boost() {
        isVip = true
        snackbar = "You're at the top for everyone"
    }

    fun appendDiscoverPage() {
        if (discoverExtras >= 6) return
        val page = discoverExtras + 1
        people.addAll(MockData.people.map { it.copy(id = "${it.id}#$page") })
        discoverExtras = page
    }

    fun reshuffleDiscover() {
        val base = people.filter { "#" !in it.id }.shuffled()
        val extra = people.filter { "#" in it.id }
        people.clear()
        people.addAll(base)
        people.addAll(extra)
    }

    fun togglePhoto(index: Int) {
        photoFilled = photoFilled.toMutableList().also { slots ->
            slots[index] = !slots[index]
        }
    }

    fun completeOnboarding() {
        myName = draftName.trim()
        myAge = ageText.toIntOrNull() ?: 18
        myGender = draftGender ?: Gender.Other
        myCountry = draftCountry ?: MockData.countries.first()
        photoFilled = listOf(draftPhoto, false, false)
        onboarded = true
    }

    fun onSplashFinished() {
        sessionCount += 1
    }

    fun maybeShowRate() {
        if (rateChecked) return
        rateChecked = true
        val now = System.currentTimeMillis()
        val cooledDown = rateDismissedAt == 0L || now - rateDismissedAt >= MockData.RATE_COOLDOWN_MS
        rateVisible = sessionCount >= MockData.RATE_AFTER_SESSIONS && cooledDown
    }

    fun dismissRate(rated: Boolean) {
        rateVisible = false
        rateDismissedAt = System.currentTimeMillis()
        if (rated) snackbar = "Thanks — the store page would open here."
    }

    fun deleteAccount() {
        onboarded = false
        onboardingStep = 0
        ageText = ""
        draftName = ""
        draftGender = null
        draftCountry = null
        draftPhoto = false
        termsAccepted = false
        myName = ""
        myAge = 0
        photoFilled = listOf(false, false, false)
        coins = MockData.STARTING_COINS
        isVip = false
        giftsReceived = MockData.GIFTS_RECEIVED
        blockedIds.clear()
        favoriteIds.clear()
        people.clear()
        people.addAll(MockData.people)
        friendIds.clear()
        friendIds.addAll(MockData.friendIds)
        requestIds.clear()
        requestIds.addAll(MockData.requestIds)
        conversations.clear()
        conversations.addAll(MockData.conversations())
        notifications.clear()
        notifications.addAll(MockData.notifications())
        discoverExtras = 0
        selectedTab = MainTab.Discover
        rateChecked = false
        confirmDeleteAccount = false
        snackbar = "Account deleted on this device"
    }

    private fun nextMessageId(): String = "m${messageSeq++}"
}

class AppViewModel : ViewModel() {
    val app = AppState()
}

val LocalAppState = staticCompositionLocalOf<AppState> { error("AppState is missing") }
