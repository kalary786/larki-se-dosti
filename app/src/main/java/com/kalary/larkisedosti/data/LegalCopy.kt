package com.kalary.larkisedosti.data

enum class LegalPage(val id: String, val title: String, val body: String) {
    Home(
        "home",
        "Larki Se Dosti",
        "Larki Se Dosti is a live way to meet people nearby and farther away. You sign in on your device, with no password.\n\nThese pages explain privacy, support, account deletion, and child safety. The same pages live in the app under Account & Privacy and Help & Support.",
    ),
    Privacy(
        "privacy",
        "Privacy Policy",
        "Larki Se Dosti keeps your profile on your device. There is no password. In this version, people, chats, gifts, and coins are sample data stored locally so you can try the app.\n\nWe ask for your age, name, gender, country, and an optional photo. Photos stay pending review until they are approved. We do not sell personal information.\n\nThe app is for adults 18 and older. If you are under 18, you cannot create an account.\n\nTerms of use: be kind, do not harass anyone, and do not upload anything you do not have the right to share. We may remove an account that breaks these rules.\n\nQuestions: support@larkisedosti.app",
    ),
    Support(
        "support",
        "Support",
        "Email support@larkisedosti.app and we will help with account access, coins, safety reports, and bugs.\n\nInclude your profile ID if you have one. You can copy it from My Profile.",
    ),
    Deletion(
        "deletion",
        "Account Deletion",
        "In the app, open Profile, then Account & Privacy, then Delete account. Confirming removes your name, photos, and local profile from this device and returns you to sign-up. Sample chats reset with the local demo data.\n\nIf you cannot open the app, email support@larkisedosti.app with the subject \"Delete my account\".",
    ),
    Safety(
        "child-safety",
        "Child Safety Standards",
        "Larki Se Dosti is strictly for adults 18 and older. Sign-up stops anyone who enters an age under 18.\n\nWe do not allow child sexual content, grooming, or any sexual or romantic content involving minors. Profiles, chat, and calls include Report and Block. Uploaded photos stay pending review until they are approved.\n\nIf you see a minor or other harmful content, report the user in the app and email support@larkisedosti.app. We will remove the content and may ban the account.",
    );

    companion object {
        fun from(id: String): LegalPage = entries.firstOrNull { it.id == id } ?: Home
    }
}
