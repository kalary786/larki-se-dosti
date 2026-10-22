package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.ui.components.ScreenHeader
import com.kalary.larkisedosti.ui.components.SettingsRow
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets

@Composable
fun HelpScreen(onBack: () -> Unit, onLegal: (String) -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val clipboard = LocalClipboardManager.current
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets(),
    ) {
        ScreenHeader("Help & Support", onBack)
        Column(
            Modifier
                .padding(Space.s16)
                .clip(RoundedCornerShape(Radius.card))
                .background(colors.surface),
        ) {
            SettingsRow(
                Lucide.Mail,
                "support@larkisedosti.app",
                subtitle = "Tap to copy",
                chevron = true,
                onClick = {
                    clipboard.setText(AnnotatedString("support@larkisedosti.app"))
                    app.snackbar = "Email copied"
                },
            )
            SettingsRow(Lucide.FileText, "Home", chevron = true, onClick = { onLegal("home") })
            SettingsRow(Lucide.Shield, "Privacy Policy", chevron = true, onClick = { onLegal("privacy") })
            SettingsRow(Lucide.CircleQuestionMark, "Support", chevron = true, onClick = { onLegal("support") })
            SettingsRow(Lucide.Trash, "Account Deletion", chevron = true, onClick = { onLegal("deletion") })
            SettingsRow(Lucide.Shield, "Child Safety Standards", chevron = true, onClick = { onLegal("child-safety") })
        }
    }
}
