package com.kalary.larkisedosti.navigation

object Routes {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Main = "main"
    const val Profile = "profile/{personId}"
    const val Chat = "chat/{personId}"
    const val Call = "call/{personId}/{video}/{incoming}"
    const val Notifications = "notifications"
    const val Coins = "coins"
    const val Edit = "edit"
    const val Account = "account"
    const val Blocked = "blocked"
    const val Help = "help"
    const val Legal = "legal/{page}"

    fun profile(id: String) = "profile/$id"
    fun chat(id: String) = "chat/$id"
    fun call(id: String, video: Boolean, incoming: Boolean) =
        "call/$id/${if (video) 1 else 0}/${if (incoming) 1 else 0}"

    fun legal(page: String) = "legal/$page"
}
