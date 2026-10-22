package com.kalary.larkisedosti.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class Gender(val label: String) { Male("Male"), Female("Female"), Other("Other") }

enum class Distance(val label: String) { Near("Near"), City("City"), State("State"), World("World") }

enum class GiftKind { Rose, Heart, Star, Bouquet, Diamond, Crown }

enum class MessageKind { Text, Emoji, Gift }

enum class NotificationKind { Request, Message, Gift, IncomingVoice, IncomingVideo }

data class Country(val name: String, val flag: String)

data class Person(
    val id: String,
    val name: String,
    val age: Int,
    val gender: Gender,
    val city: String,
    val region: String,
    val country: String,
    val flag: String,
    val bio: String,
    val online: Boolean,
    val verified: Boolean,
    val distance: Distance,
    val angle: Float,
    val portrait: Int,
    val photoCount: Int,
    val letter: Boolean,
)

data class Gift(
    val id: String,
    val name: String,
    val cost: Int,
    val kind: GiftKind,
)

data class CoinPack(val coins: Int, val price: String, val best: Boolean)

data class ChatMessage(
    val id: String,
    val mine: Boolean,
    val kind: MessageKind,
    val text: String,
    val time: String,
    val giftId: String? = null,
)

class Conversation(
    val personId: String,
    messages: List<ChatMessage>,
    unreadCount: Int,
) {
    val messages = mutableStateListOf<ChatMessage>().apply { addAll(messages) }
    var unread by mutableIntStateOf(unreadCount)
    var muted by mutableStateOf(false)

    val preview: String
        get() = messages.lastOrNull()?.let { message ->
            if (message.kind == MessageKind.Gift) "Gift · ${message.text}" else message.text
        } ?: "Say hello"

    val timeLabel: String
        get() = messages.lastOrNull()?.time ?: ""
}

data class AppNotification(
    val id: String,
    val personId: String,
    val title: String,
    val body: String,
    val time: String,
    val kind: NotificationKind,
    val unread: Boolean,
)

object MockData {
    const val USER_ID = "181"
    const val STARTING_COINS = 8
    const val CALL_COST = 10
    const val VIDEO_COINS = 15
    const val RATE_AFTER_SESSIONS = 3
    const val RATE_COOLDOWN_MS = 3L * 24 * 60 * 60 * 1000
    const val LEVEL = 4
    const val GIFTS_RECEIVED = 7

    val countries = listOf(
        Country("Pakistan", "🇵🇰"),
        Country("India", "🇮🇳"),
        Country("Bangladesh", "🇧🇩"),
        Country("Nepal", "🇳🇵"),
        Country("Sri Lanka", "🇱🇰"),
        Country("United Arab Emirates", "🇦🇪"),
        Country("Saudi Arabia", "🇸🇦"),
        Country("Qatar", "🇶🇦"),
        Country("United Kingdom", "🇬🇧"),
        Country("Canada", "🇨🇦"),
        Country("United States", "🇺🇸"),
        Country("South Korea", "🇰🇷"),
        Country("Jordan", "🇯🇴"),
        Country("Turkey", "🇹🇷"),
        Country("Malaysia", "🇲🇾"),
        Country("Indonesia", "🇮🇩"),
        Country("Australia", "🇦🇺"),
        Country("Germany", "🇩🇪"),
        Country("France", "🇫🇷"),
        Country("Egypt", "🇪🇬"),
        Country("Nigeria", "🇳🇬"),
        Country("South Africa", "🇿🇦"),
        Country("Japan", "🇯🇵"),
        Country("Brazil", "🇧🇷"),
    )

    val people = listOf(
        Person("ayesha", "Ayesha Khan", 24, Gender.Female, "Lahore", "Punjab", "Pakistan", "🇵🇰", "Evening chai, long voice notes, and someone who actually listens.", true, true, Distance.Near, 210f, 0, 3, false),
        Person("meher", "Meher Fatima", 22, Gender.Female, "Karachi", "Sindh", "Pakistan", "🇵🇰", "Sea breeze, late playlists, and friends who text back.", true, false, Distance.Near, 330f, 1, 2, false),
        Person("sana", "Sana Iqbal", 27, Gender.Female, "Islamabad", "ICT", "Pakistan", "🇵🇰", "Bookshops, quiet calls, and weekend hikes when the air is clear.", false, true, Distance.City, 30f, 2, 3, false),
        Person("priya", "Priya Sharma", 23, Gender.Female, "Delhi", "Delhi", "India", "🇮🇳", "I send gifts before I send essays. Ask me about street food.", true, true, Distance.City, 160f, 3, 3, false),
        Person("ananya", "Ananya Rao", 21, Gender.Female, "Mumbai", "Maharashtra", "India", "🇮🇳", "Film stills, monsoon walks, and a terrible singing voice.", true, false, Distance.State, 70f, 4, 2, false),
        Person("fatima", "Fatima Noor", 25, Gender.Female, "Dhaka", "Dhaka", "Bangladesh", "🇧🇩", "Poetry in the margins and friends who stay for the second cup.", false, true, Distance.State, 200f, 5, 1, true),
        Person("hassan", "Hassan Ali", 28, Gender.Male, "Lahore", "Punjab", "Pakistan", "🇵🇰", "Cricket on the radio and honest advice after midnight.", true, true, Distance.Near, 140f, 6, 3, false),
        Person("omar", "Omar Farooq", 24, Gender.Male, "Dubai", "Dubai", "United Arab Emirates", "🇦🇪", "New in the group chat. I cook better than I photograph.", true, false, Distance.World, 20f, 7, 2, true),
        Person("bilal", "Bilal Ahmed", 29, Gender.Male, "London", "England", "United Kingdom", "🇬🇧", "Rain, night buses, and calls that run longer than planned.", false, false, Distance.World, 110f, 8, 2, false),
        Person("zara", "Zara Malik", 23, Gender.Female, "Toronto", "Ontario", "Canada", "🇨🇦", "Snow days, voice memos, and friends across too many time zones.", true, true, Distance.City, 300f, 9, 3, false),
        Person("noah", "Noah Kim", 26, Gender.Male, "Seoul", "Seoul", "South Korea", "🇰🇷", "Late studios, quiet humor, and a soft spot for bad puns.", false, false, Distance.State, 350f, 10, 1, true),
        Person("layla", "Layla Haddad", 22, Gender.Female, "Amman", "Amman", "Jordan", "🇯🇴", "Balcony tea, old songs, and people who mean what they say.", true, true, Distance.World, 230f, 11, 2, false),
    )

    val gifts = listOf(
        Gift("rose", "Rose", 5, GiftKind.Rose),
        Gift("heart", "Heart", 10, GiftKind.Heart),
        Gift("star", "Star", 15, GiftKind.Star),
        Gift("bouquet", "Bouquet", 25, GiftKind.Bouquet),
        Gift("diamond", "Diamond", 50, GiftKind.Diamond),
        Gift("crown", "Crown", 100, GiftKind.Crown),
    )

    val coinPacks = listOf(
        CoinPack(50, "$0.99", false),
        CoinPack(120, "$1.99", false),
        CoinPack(300, "$4.99", true),
        CoinPack(700, "$9.99", false),
        CoinPack(1500, "$19.99", false),
    )

    val friendIds = listOf("hassan", "zara", "meher")
    val requestIds = listOf("omar", "layla")

    val replies = listOf(
        "I'm here.",
        "Tell me more.",
        "That made me smile.",
        "Give me one minute.",
        "Okay, your turn.",
    )

    fun conversations(): List<Conversation> = listOf(
        Conversation(
            personId = "ayesha",
            unreadCount = 2,
            messages = listOf(
                ChatMessage("a1", false, MessageKind.Text, "You around tonight?", "6:40 PM"),
                ChatMessage("a2", true, MessageKind.Text, "Maybe after dinner.", "6:44 PM"),
                ChatMessage("a3", false, MessageKind.Text, "Are you free after 9?", "6:51 PM"),
                ChatMessage("a4", false, MessageKind.Emoji, "😊", "6:52 PM"),
            ),
        ),
        Conversation(
            personId = "priya",
            unreadCount = 1,
            messages = listOf(
                ChatMessage("p1", false, MessageKind.Gift, "Rose", "5:10 PM", giftId = "rose"),
                ChatMessage("p2", true, MessageKind.Text, "That was sweet.", "5:18 PM"),
                ChatMessage("p3", false, MessageKind.Text, "You picked the right one.", "5:21 PM"),
            ),
        ),
        Conversation(
            personId = "hassan",
            unreadCount = 0,
            messages = listOf(
                ChatMessage("h1", true, MessageKind.Text, "Still in Lahore?", "Yesterday"),
                ChatMessage("h2", false, MessageKind.Text, "Lahore traffic is wild today.", "Yesterday"),
            ),
        ),
        Conversation(
            personId = "bilal",
            unreadCount = 0,
            messages = listOf(
                ChatMessage("b1", false, MessageKind.Text, "Call me when you're around.", "Mon"),
                ChatMessage("b2", true, MessageKind.Text, "Will do.", "Mon"),
            ),
        ),
    )

    fun notifications(): List<AppNotification> = listOf(
        AppNotification("n1", "omar", "Friend request", "Omar Farooq wants to be friends.", "2m", NotificationKind.Request, true),
        AppNotification("n2", "ayesha", "New message", "Ayesha Khan sent you a message.", "5m", NotificationKind.Message, true),
        AppNotification("n3", "priya", "Gift received", "Priya Sharma sent you a Rose.", "1h", NotificationKind.Gift, true),
        AppNotification("n4", "hassan", "Incoming voice call", "Hassan Ali is calling.", "3h", NotificationKind.IncomingVoice, false),
        AppNotification("n5", "sana", "Incoming video call", "Sana Iqbal is calling.", "Yesterday", NotificationKind.IncomingVideo, true),
    )

    fun gift(id: String): Gift = gifts.first { it.id == id }
}
