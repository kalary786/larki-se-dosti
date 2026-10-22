package com.larkisedosti.app.ui.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.components.AdSlot
import com.larkisedosti.app.ui.components.Avatar
import com.larkisedosti.app.ui.components.Badge
import com.larkisedosti.app.ui.components.BottomSheet
import com.larkisedosti.app.ui.components.CoinPill
import com.larkisedosti.app.ui.components.EmptyState
import com.larkisedosti.app.ui.components.FilterChip
import com.larkisedosti.app.ui.components.GhostButton
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.ModalDialog
import com.larkisedosti.app.ui.components.PrimaryButton
import com.larkisedosti.app.ui.components.ProfileCard
import com.larkisedosti.app.ui.components.SecondaryButton
import com.larkisedosti.app.ui.components.SettingsRow
import com.larkisedosti.app.ui.components.SettingsTrailing
import com.larkisedosti.app.ui.components.StatTile
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ComponentGalleryScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val colors = LoungeTheme.colors
    var coins by rememberSaveable { mutableIntStateOf(240) }
    var filter by rememberSaveable { mutableStateOf("Nearby") }
    var sheetOpen by rememberSaveable { mutableStateOf(false) }
    var dialogOpen by rememberSaveable { mutableStateOf(false) }
    var notifications by rememberSaveable { mutableStateOf(true) }
    val filters = listOf("Nearby", "New", "Online")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = LoungeSpace.s20)
            .padding(top = LoungeSpace.s16, bottom = LoungeSpace.s32),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Larki Se Dosti",
                    color = colors.textPrimary,
                    style = LoungeType.h1,
                )
                Spacer(Modifier.height(LoungeSpace.s4))
                Text(
                    text = if (darkTheme) "Night lounge" else "Day lounge",
                    color = colors.textSecondary,
                    style = LoungeType.body,
                )
            }
            IconCircleButton(
                icon = if (darkTheme) AppIcons.Sun else AppIcons.Moon,
                contentDescription = "Switch theme",
                onClick = onToggleTheme,
            )
        }

        Spacer(Modifier.height(LoungeSpace.s16))
        CoinPill(balance = coins, onAddClick = { coins += 50 })

        SectionLabel("Buttons")
        PrimaryButton(
            text = "Say hello",
            icon = AppIcons.Heart,
            onClick = { dialogOpen = true },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(LoungeSpace.s8))
        SecondaryButton(
            text = "Filters",
            icon = AppIcons.Sliders,
            onClick = { sheetOpen = true },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(LoungeSpace.s8))
        GhostButton(
            text = "Maybe later",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )

        SectionLabel("Chips")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
            verticalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
        ) {
            filters.forEach { label ->
                FilterChip(
                    label = label,
                    selected = filter == label,
                    onClick = { filter = label },
                )
            }
        }

        SectionLabel("People")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(name = "Areeba", online = true, ring = true)
            Spacer(Modifier.width(LoungeSpace.s12))
            Avatar(name = "Hania Khan", size = 56.dp)
            Spacer(Modifier.width(LoungeSpace.s12))
            Box {
                IconCircleButton(
                    icon = AppIcons.Bell,
                    contentDescription = "Notifications",
                    onClick = {},
                )
                Badge(
                    count = 3,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp),
                )
            }
        }

        Spacer(Modifier.height(LoungeSpace.s16))
        ProfileCard(
            name = "Areeba",
            age = 24,
            subtitle = "Lahore · 2 km",
            online = true,
            tags = listOf("Coffee", "Night walks", "Books"),
            onClick = {},
        )

        SectionLabel("Stats")
        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s12)) {
            StatTile(
                value = "18",
                label = "Matches",
                icon = AppIcons.Heart,
                modifier = Modifier.weight(1f),
            )
            StatTile(
                value = "6",
                label = "Chats",
                icon = AppIcons.Message,
                modifier = Modifier.weight(1f),
            )
        }

        SectionLabel("Ad")
        AdSlot()

        SectionLabel("Settings")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(LoungeShape.card)
                .background(colors.surface)
                .border(1.dp, colors.border, LoungeShape.card),
        ) {
            SettingsRow(
                icon = AppIcons.Bell,
                title = "Notifications",
                subtitle = "New messages and matches",
                trailing = SettingsTrailing.Toggle(
                    checked = notifications,
                    onCheckedChange = { notifications = it },
                ),
                showDivider = true,
            )
            SettingsRow(
                icon = AppIcons.User,
                title = "Edit profile",
                subtitle = "Photos, bio, interests",
                onClick = {},
                showDivider = true,
            )
            SettingsRow(
                icon = AppIcons.Settings,
                title = "Preferences",
                subtitle = "Discovery and privacy",
                onClick = { sheetOpen = true },
            )
        }

        SectionLabel("Empty")
        EmptyState(
            title = "No messages yet",
            message = "When someone writes back, the conversation will show up here.",
            icon = AppIcons.Inbox,
            actionLabel = "Find people",
            onAction = {},
        )
    }

    BottomSheet(visible = sheetOpen, onDismiss = { sheetOpen = false }) {
        Text(text = "Filters", color = colors.textPrimary, style = LoungeType.h2)
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(
            text = "Show people who match what you are in the mood for.",
            color = colors.textSecondary,
            style = LoungeType.body,
        )
        Spacer(Modifier.height(LoungeSpace.s16))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
            verticalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
        ) {
            filters.forEach { label ->
                FilterChip(
                    label = label,
                    selected = filter == label,
                    onClick = { filter = label },
                )
            }
        }
        Spacer(Modifier.height(LoungeSpace.s24))
        PrimaryButton(
            text = "Apply",
            icon = AppIcons.Check,
            onClick = { sheetOpen = false },
            modifier = Modifier.fillMaxWidth(),
        )
    }

    ModalDialog(
        visible = dialogOpen,
        title = "Send a hello?",
        body = "This uses 20 coins. Nothing is sent until you confirm.",
        confirmLabel = "Send",
        dismissLabel = "Not now",
        onConfirm = {
            if (coins >= 20) coins -= 20
            dialogOpen = false
        },
        onDismiss = { dialogOpen = false },
    )
}

@Composable
private fun SectionLabel(text: String) {
    Spacer(Modifier.height(LoungeSpace.s32))
    Text(
        text = text,
        color = LoungeTheme.colors.textSecondary,
        style = LoungeType.caption,
    )
    Spacer(Modifier.height(LoungeSpace.s12))
}
