package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MainTab
import com.kalary.larkisedosti.ui.components.GhostButton
import com.kalary.larkisedosti.ui.components.IconCircleButton
import com.kalary.larkisedosti.ui.components.PhotoSlot
import com.kalary.larkisedosti.ui.components.PrimaryButton
import com.kalary.larkisedosti.ui.components.SettingsRow
import com.kalary.larkisedosti.ui.components.StatTile
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.tag
import kotlinx.coroutines.launch

@Composable
fun MyProfileScreen(
    onEdit: () -> Unit,
    onCoins: () -> Unit,
    onAccount: () -> Unit,
    onHelp: () -> Unit,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = Space.s16)
            .padding(bottom = Space.s16)
            .tag("screen_profile_tab"),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(app.myName.ifBlank { "Your name" }, style = AppType.h1, color = colors.textPrimary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("ID ${app.userId}", style = AppType.body, color = colors.textSecondary)
                    GhostButton("Copy", {
                        clipboard.setText(AnnotatedString(app.userId))
                        app.snackbar = "ID copied"
                    })
                }
            }
            IconCircleButton(Lucide.Settings, "Settings", { scope.launch { scroll.animateScrollTo(scroll.maxValue) } })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Space.s8), modifier = Modifier.padding(top = Space.s8)) {
            Chip(app.myGender.label)
            Chip("${app.myCountry.flag} ${app.myCountry.name}")
            if (app.isVip) {
                Text(
                    "VIP",
                    style = AppType.caption,
                    color = colors.onPrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.chip))
                        .background(colors.primary)
                        .padding(horizontal = Space.s12, vertical = Space.s8),
                )
            }
        }
        Text("Add up to 3 photos", style = AppType.title, color = colors.textPrimary, modifier = Modifier.padding(top = Space.s20, bottom = Space.s8))
        Row(horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
            app.photoFilled.forEachIndexed { index, filled ->
                PhotoSlot(filled, seed = index + 3, onClick = { app.togglePhoto(index) }, modifier = Modifier.weight(1f))
            }
        }
        InfoCard(
            "Photos stay pending review until they are approved.",
            Modifier.padding(top = Space.s16),
        )
        Row(Modifier.padding(top = Space.s16), horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
            StatTile(app.giftsReceived.toString(), "Gifts", Modifier.weight(1f))
            StatTile(app.friendIds.size.toString(), "Friends", Modifier.weight(1f))
        }
        Row(Modifier.padding(top = Space.s8), horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
            StatTile(app.level.toString(), "Level", Modifier.weight(1f))
            StatTile(app.coins.toString(), "My Coins", Modifier.weight(1f))
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = Space.s16)
                .clip(RoundedCornerShape(Radius.card))
                .background(colors.surfaceElevated)
                .border(1.dp, colors.border, RoundedCornerShape(Radius.card))
                .padding(Space.s16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Lucide.Crown, contentDescription = null, tint = colors.secondary)
            Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
                Text("Boost Profile", style = AppType.title, color = colors.textPrimary)
                Text(
                    if (app.isVip) "You're showing at the top" else "Get VIP · show on top for everyone",
                    style = AppType.caption,
                    color = colors.textSecondary,
                )
            }
            PrimaryButton(if (app.isVip) "Boosted" else "Boost", { app.boost() }, enabled = !app.isVip)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = Space.s16)
                .clip(RoundedCornerShape(Radius.card))
                .background(colors.surface),
        ) {
            SettingsRow(Lucide.Pencil, "Edit Profile", subtitle = "Name, country, gender, photo", chevron = true, onClick = onEdit)
            SettingsRow(Lucide.Crown, "Boost Profile", subtitle = "VIP badge, appear first", chevron = true, onClick = { app.boost() })
            SettingsRow(Lucide.Coins, "My Coins", subtitle = "Buy coins", chevron = true, onClick = onCoins)
            SettingsRow(Lucide.Users, "My Friends", chevron = true, onClick = { app.selectedTab = MainTab.Friends })
            SettingsRow(Lucide.Bell, "Notifications", checked = app.notificationsOn, onCheckedChange = { app.notificationsOn = it })
            SettingsRow(Lucide.Volume2, "Chat sounds", checked = app.chatSounds, onCheckedChange = { app.chatSounds = it })
            SettingsRow(
                if (app.darkTheme) Lucide.Moon else Lucide.Sun,
                "Theme",
                subtitle = if (app.darkTheme) "Dark" else "Light",
                checked = app.darkTheme,
                onCheckedChange = { app.darkTheme = it },
            )
            SettingsRow(Lucide.Shield, "Account & Privacy", subtitle = "Blocked users, policy, delete account", chevron = true, onClick = onAccount)
            SettingsRow(Lucide.CircleQuestionMark, "Help & Support", chevron = true, onClick = onHelp)
        }
    }
}

@Composable
private fun Chip(text: String) {
    val colors = LarkiTheme.colors
    Text(
        text,
        style = AppType.caption,
        color = colors.textPrimary,
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.chip))
            .background(colors.surfaceElevated)
            .border(1.dp, colors.border, RoundedCornerShape(Radius.chip))
            .padding(horizontal = Space.s12, vertical = Space.s8),
    )
}

@Composable
fun InfoCard(text: String, modifier: Modifier = Modifier) {
    val colors = LarkiTheme.colors
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.card))
            .background(colors.surface)
            .border(1.dp, colors.border, RoundedCornerShape(Radius.card))
            .padding(Space.s12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Lucide.Info, contentDescription = null, tint = colors.primary)
        Text(text, style = AppType.body, color = colors.textSecondary, modifier = Modifier.padding(start = Space.s12))
    }
}
