package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.ui.components.ScreenHeader
import com.kalary.larkisedosti.ui.components.SettingsRow
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets

@Composable
fun AccountPrivacyScreen(
    onBack: () -> Unit,
    onBlocked: () -> Unit,
    onLegal: (String) -> Unit,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets(),
    ) {
        ScreenHeader("Account & Privacy", onBack)
        Column(
            Modifier
                .padding(Space.s16)
                .clip(RoundedCornerShape(Radius.card))
                .background(colors.surface),
        ) {
            SettingsRow(Lucide.Ban, "Blocked users", subtitle = "People you have blocked", chevron = true, onClick = onBlocked)
            SettingsRow(Lucide.FileText, "Home", chevron = true, onClick = { onLegal("home") })
            SettingsRow(Lucide.Shield, "Privacy Policy", chevron = true, onClick = { onLegal("privacy") })
            SettingsRow(Lucide.Mail, "Support", subtitle = "support@larkisedosti.app", chevron = true, onClick = { onLegal("support") })
            SettingsRow(Lucide.FileText, "Account Deletion", chevron = true, onClick = { onLegal("deletion") })
            SettingsRow(Lucide.Shield, "Child Safety Standards", chevron = true, onClick = { onLegal("child-safety") })
            SettingsRow(
                Lucide.Trash,
                "Delete account",
                subtitle = "Removes your profile from this device",
                titleColor = colors.danger,
                chevron = true,
                onClick = { app.confirmDeleteAccount = true },
            )
        }
    }
}
