package com.larkisedosti.app.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.larkisedosti.app.ui.icons.AppIcons

enum class Gender(val label: String) {
    Male("Male"),
    Female("Female"),
    Other("Other"),
}

enum class PhotoStatus {
    Pending,
    Approved,
}

data class ProfilePhoto(
    val uri: String,
    val status: PhotoStatus = PhotoStatus.Pending,
)

data class UserReport(
    val personId: String,
    val reason: String,
    val note: String,
)

data class Country(
    val name: String,
    val flag: String,
    val city: String,
    val region: String,
)

data class Person(
    val id: String,
    val name: String,
    val age: Int,
    val gender: Gender,
    val city: String,
    val region: String,
    val countryName: String,
    val countryFlag: String,
    val online: Boolean,
    val bio: String,
    val verified: Boolean,
    val ring: Int,
    val bearing: Float,
    val color: Color,
    val panels: Int,
    val photoUrl: String? = null,
)

data class ChatMessage(
    val id: String,
    val fromMe: Boolean,
    val text: String,
    val time: String,
    val gift: GiftKind? = null,
    val sentAt: Long = System.currentTimeMillis(),
)

enum class GiftKind(
    val title: String,
    val price: Int,
) {
    Rose("Rose", 5),
    Heart("Heart", 10),
    Star("Star", 15),
    Bouquet("Bouquet", 25),
    Diamond("Diamond", 50),
    Crown("Crown", 100),
    ;

    val icon: ImageVector
        get() = when (this) {
            Rose -> AppIcons.Rose
            Heart -> AppIcons.Heart
            Star -> AppIcons.Star
            Bouquet -> AppIcons.Bouquet
            Diamond -> AppIcons.Diamond
            Crown -> AppIcons.Crown
        }
}

class Conversation(
    val personId: String,
    unreadCount: Int,
    time: String,
) {
    val messages = mutableStateListOf<ChatMessage>()
    var unread by mutableIntStateOf(unreadCount)
    var timeLabel by mutableStateOf(time)
}

class Notice(
    val id: String,
    val title: String,
    val body: String,
    val time: String,
    val kind: String,
    val personId: String?,
    readInitial: Boolean = false,
) {
    var read by mutableStateOf(readInitial)
}

val Countries = listOf(
    Country("Pakistan", "\uD83C\uDDF5\uD83C\uDDF0", "Lahore", "Punjab"),
    Country("India", "\uD83C\uDDEE\uD83C\uDDF3", "Mumbai", "Maharashtra"),
    Country("Bangladesh", "\uD83C\uDDE7\uD83C\uDDE9", "Dhaka", "Dhaka"),
    Country("United Arab Emirates", "\uD83C\uDDE6\uD83C\uDDEA", "Dubai", "Dubai"),
    Country("Saudi Arabia", "\uD83C\uDDF8\uD83C\uDDE6", "Jeddah", "Makkah"),
    Country("United Kingdom", "\uD83C\uDDEC\uD83C\uDDE7", "London", "England"),
    Country("United States", "\uD83C\uDDFA\uD83C\uDDF8", "Austin", "Texas"),
    Country("Canada", "\uD83C\uDDE8\uD83C\uDDE6", "Toronto", "Ontario"),
    Country("Turkey", "\uD83C\uDDF9\uD83C\uDDF7", "Istanbul", "Marmara"),
    Country("Malaysia", "\uD83C\uDDF2\uD83C\uDDFE", "Kuala Lumpur", "Kuala Lumpur"),
    Country("Indonesia", "\uD83C\uDDEE\uD83C\uDDE9", "Jakarta", "Jakarta"),
    Country("Egypt", "\uD83C\uDDEA\uD83C\uDDEC", "Cairo", "Cairo"),
    Country("Germany", "\uD83C\uDDE9\uD83C\uDDEA", "Berlin", "Berlin"),
    Country("France", "\uD83C\uDDEB\uD83C\uDDF7", "Lyon", "Auvergne-Rhône-Alpes"),
    Country("Australia", "\uD83C\uDDE6\uD83C\uDDFA", "Sydney", "New South Wales"),
    Country("Nigeria", "\uD83C\uDDF3\uD83C\uDDEC", "Lagos", "Lagos"),
    Country("South Africa", "\uD83C\uDDFF\uD83C\uDDE6", "Cape Town", "Western Cape"),
    Country("Brazil", "\uD83C\uDDE7\uD83C\uDDF7", "São Paulo", "São Paulo"),
    Country("Japan", "\uD83C\uDDEF\uD83C\uDDF5", "Osaka", "Kansai"),
    Country("South Korea", "\uD83C\uDDF0\uD83C\uDDF7", "Seoul", "Seoul"),
)
