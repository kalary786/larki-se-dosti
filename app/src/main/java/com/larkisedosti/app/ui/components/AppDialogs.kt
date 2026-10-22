package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.theme.appear

private val reportReasons = listOf(
    "Fake profile",
    "Harassment",
    "Spam",
    "Inappropriate content",
    "Scam or money request",
    "Other",
)

@Composable
fun NotEnoughCoinsModal(
    visible: Boolean,
    balance: Int,
    needed: Int,
    forCall: Boolean,
    onBuyCoins: () -> Unit,
    onFreeTry: () -> Unit,
    onDismiss: () -> Unit,
) {
    var watching by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(visible) {
        if (!visible) watching = false
    }
    if (!visible) return
    val colors = LoungeTheme.colors
    val costLine = if (forCall) {
        "This call costs $needed coins"
    } else {
        "This gift costs $needed coins"
    }
    val freeLabel = if (forCall) "Watch video — free call" else "Watch video — free gift"
    DialogShell(onDismiss) {
        if (watching) {
            IconBadge(icon = AppIcons.Video, tint = colors.primary)
            Spacer(Modifier.height(LoungeSpace.s16))
            Text(text = "One free try", color = colors.textPrimary, style = LoungeType.h2, textAlign = TextAlign.Center)
            Spacer(Modifier.height(LoungeSpace.s8))
            Text(
                text = "A short video plays here. Finish it and this one goes through without coins.",
                color = colors.textSecondary,
                style = LoungeType.body,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(LoungeSpace.s24))
            PrimaryButton(
                text = "Finish",
                onClick = {
                    watching = false
                    onFreeTry()
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(LoungeSpace.s8))
            GhostButton(
                text = "Not now",
                onClick = { watching = false },
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            IconBadge(icon = AppIcons.Coin, tint = colors.coin)
            Spacer(Modifier.height(LoungeSpace.s16))
            Text(
                text = "Not enough coins",
                color = colors.textPrimary,
                style = LoungeType.h2,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(LoungeSpace.s8))
            Text(
                text = "Buy coins, or watch a video for one free try",
                color = colors.textSecondary,
                style = LoungeType.body,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(LoungeSpace.s16))
            Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
                CoinStat(label = "You have", value = balance, modifier = Modifier.weight(1f))
                CoinStat(label = "Need", value = needed, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(LoungeSpace.s12))
            Text(
                text = costLine,
                color = colors.textSecondary,
                style = LoungeType.caption,
                modifier = Modifier
                    .clip(LoungeShape.chip)
                    .background(colors.surface)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
            Spacer(Modifier.height(LoungeSpace.s20))
            PrimaryButton(text = "Buy Coins", onClick = onBuyCoins, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(LoungeSpace.s8))
            SecondaryButton(text = freeLabel, onClick = { watching = true }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(LoungeSpace.s8))
            GhostButton(text = "Not now", onClick = onDismiss, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun RateAppModal(
    visible: Boolean,
    onLater: () -> Unit,
    onRate: () -> Unit,
) {
    if (!visible) return
    val colors = LoungeTheme.colors
    DialogShell(onLater) {
        IconBadge(icon = AppIcons.Star, tint = colors.primary)
        Spacer(Modifier.height(LoungeSpace.s16))
        Text(
            text = "Rate Larki Se Dosti",
            color = colors.textPrimary,
            style = LoungeType.h2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(
            text = "If the room feels right, a rating helps other people find it.",
            color = colors.textSecondary,
            style = LoungeType.body,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(LoungeSpace.s24))
        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
            SecondaryButton(text = "Later", onClick = onLater, modifier = Modifier.weight(1f))
            PrimaryButton(text = "Rate now", onClick = onRate, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ReportUserDialog(
    visible: Boolean,
    name: String,
    onDismiss: () -> Unit,
    onSubmit: (reason: String, note: String) -> Unit,
) {
    var reason by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    if (!visible) return
    val colors = LoungeTheme.colors
    DialogShell(onDismiss) {
        Text(text = "Report $name", color = colors.textPrimary, style = LoungeType.h2)
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(
            text = "Choose what happened. A note is optional.",
            color = colors.textSecondary,
            style = LoungeType.body,
        )
        Spacer(Modifier.height(LoungeSpace.s16))
        reportReasons.forEach { option ->
            val selected = option == reason
            Text(
                text = option,
                color = if (selected) colors.onPrimary else colors.textPrimary,
                style = LoungeType.body,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LoungeSpace.s8)
                    .clip(LoungeShape.input)
                    .background(if (selected) colors.primary else colors.surface)
                    .border(1.dp, if (selected) colors.primary else colors.border, LoungeShape.input)
                    .clickable { reason = option }
                    .padding(horizontal = LoungeSpace.s16, vertical = 12.dp),
            )
        }
        LoungeTextField(
            value = note,
            onValueChange = { note = it },
            placeholder = "Add a detail (optional)",
            singleLine = false,
        )
        Spacer(Modifier.height(LoungeSpace.s16))
        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
            GhostButton(text = "Not now", onClick = onDismiss, modifier = Modifier.weight(1f))
            PrimaryButton(
                text = "Submit",
                enabled = reason.isNotEmpty(),
                onClick = {
                    onSubmit(reason, note.trim())
                    reason = ""
                    note = ""
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun BlockUserDialog(
    visible: Boolean,
    name: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (!visible) return
    val colors = LoungeTheme.colors
    DialogShell(onDismiss) {
        IconBadge(icon = AppIcons.Ban, tint = colors.danger)
        Spacer(Modifier.height(LoungeSpace.s16))
        Text(
            text = "Block $name?",
            color = colors.textPrimary,
            style = LoungeType.h2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(
            text = "They can't message you, and they leave Discover, Match, and your chats.",
            color = colors.textSecondary,
            style = LoungeType.body,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(LoungeSpace.s24))
        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
            SecondaryButton(text = "Not now", onClick = onDismiss, modifier = Modifier.weight(1f))
            LoungeButton(
                text = "Block",
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                enabled = true,
                icon = null,
                container = colors.danger,
                content = colors.onPrimary,
                border = null,
            )
        }
    }
}

@Composable
fun DeleteChatDialog(
    visible: Boolean,
    name: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (!visible) return
    val colors = LoungeTheme.colors
    DialogShell(onDismiss) {
        IconBadge(icon = AppIcons.Trash, tint = colors.danger)
        Spacer(Modifier.height(LoungeSpace.s16))
        Text(
            text = "Delete this chat?",
            color = colors.textPrimary,
            style = LoungeType.h2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(
            text = "Messages with $name are removed from this device. They are not blocked.",
            color = colors.textSecondary,
            style = LoungeType.body,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(LoungeSpace.s24))
        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
            SecondaryButton(text = "Not now", onClick = onDismiss, modifier = Modifier.weight(1f))
            LoungeButton(
                text = "Delete",
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                enabled = true,
                icon = null,
                container = colors.danger,
                content = colors.onPrimary,
                border = null,
            )
        }
    }
}

@Composable
private fun DialogShell(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    val colors = LoungeTheme.colors
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .appear()
                .padding(horizontal = LoungeSpace.s24)
                .fillMaxWidth()
                .clip(LoungeShape.card)
                .background(colors.surfaceElevated)
                .verticalScroll(rememberScrollState())
                .padding(LoungeSpace.s24),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    }
}

@Composable
private fun IconBadge(
    icon: ImageVector,
    tint: Color,
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(LoungeTheme.colors.surface),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(26.dp))
    }
}

@Composable
private fun CoinStat(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    val colors = LoungeTheme.colors
    Column(
        modifier = modifier
            .clip(LoungeShape.input)
            .background(colors.surface)
            .border(1.dp, colors.border, LoungeShape.input)
            .padding(LoungeSpace.s12),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = AppIcons.Coin,
                contentDescription = null,
                tint = colors.coin,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.size(6.dp))
            Text(text = value.toString(), color = colors.textPrimary, style = LoungeType.title)
        }
        Spacer(Modifier.height(4.dp))
        Text(text = label, color = colors.textSecondary, style = LoungeType.caption)
    }
}
