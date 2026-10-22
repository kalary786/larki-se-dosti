package com.larkisedosti.app.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.PhotoStatus
import com.larkisedosti.app.model.ProfilePhoto
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.ModalDialog
import com.larkisedosti.app.ui.components.PrimaryButton
import com.larkisedosti.app.ui.components.SettingsRow
import com.larkisedosti.app.ui.components.SettingsTrailing
import com.larkisedosti.app.ui.components.StatTile
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.shell.LocalShellInset
import kotlinx.coroutines.delay

@Composable
fun MyProfileScreen(
    store: AppStore,
    onEdit: () -> Unit,
    onCoins: () -> Unit,
    onFriends: () -> Unit,
    onPrivacy: () -> Unit,
    onHelp: () -> Unit,
) {
    val colors = LoungeTheme.colors
    val clipboard = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    var boostOpen by remember { mutableStateOf(false) }
    var slot by remember { mutableIntStateOf(0) }
    val picker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null && slot in 0..2) store.setPhoto(slot, uri.toString())
    }
    LaunchedEffect(copied) {
        if (copied) {
            delay(1200)
            copied = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = LoungeSpace.s16, vertical = LoungeSpace.s12)
            .padding(bottom = LocalShellInset.current),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = store.name.ifBlank { "You" }, color = colors.textPrimary, style = LoungeType.h1)
                if (store.username.isNotBlank()) {
                    Text(text = "@${store.username}", color = colors.textSecondary, style = LoungeType.body)
                }
                if (store.shortId > 0L) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "ID ${store.shortId}", color = colors.textSecondary, style = LoungeType.body)
                        Spacer(Modifier.size(LoungeSpace.s8))
                        Text(
                            text = if (copied) "Copied" else "Copy",
                            color = colors.primary,
                            style = LoungeType.caption.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.clickable {
                                clipboard.setText(AnnotatedString(store.shortId.toString()))
                                copied = true
                            },
                        )
                    }
                }
            }
            IconCircleButton(
                icon = AppIcons.Settings,
                contentDescription = "Settings",
                onClick = onEdit,
            )
        }
        Spacer(Modifier.height(LoungeSpace.s12))
        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
            InfoChip(store.gender.label)
            InfoChip("${store.country.flag}  ${store.country.name}")
        }
        Spacer(Modifier.height(LoungeSpace.s20))
        Text(text = "Add up to 3 photos", color = colors.textPrimary, style = LoungeType.title)
        Spacer(Modifier.height(LoungeSpace.s12))
        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
            repeat(3) { index ->
                PhotoSlot(
                    photo = store.photos.getOrNull(index),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        slot = index
                        picker.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    },
                )
            }
        }
        Spacer(Modifier.height(LoungeSpace.s12))
        Text(
            text = "Your photo will be visible to others after review. Until then, people see your name.",
            color = colors.textSecondary,
            style = LoungeType.body,
            modifier = Modifier
                .fillMaxWidth()
                .clip(LoungeShape.card)
                .background(colors.surface)
                .border(1.dp, colors.border, LoungeShape.card)
                .padding(LoungeSpace.s16),
        )
        Spacer(Modifier.height(LoungeSpace.s16))
        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
            StatTile(value = store.giftsSent.toString(), label = "Gifts", compact = true, modifier = Modifier.weight(1f).fillMaxWidth())
            StatTile(value = store.friendIds.size.toString(), label = "Friends", compact = true, modifier = Modifier.weight(1f).fillMaxWidth())
            StatTile(value = store.level.toString(), label = "Level", compact = true, modifier = Modifier.weight(1f).fillMaxWidth())
            StatTile(value = store.coins.toString(), label = "Coins", compact = true, modifier = Modifier.weight(1f).fillMaxWidth())
        }
        Spacer(Modifier.height(LoungeSpace.s16))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(LoungeShape.card)
                .background(colors.surface)
                .border(1.dp, colors.border, LoungeShape.card)
                .padding(LoungeSpace.s16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceElevated),
                contentAlignment = Alignment.Center,
            ) {
                Icon(AppIcons.Crown, contentDescription = null, tint = colors.coin, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.size(LoungeSpace.s12))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Boost Profile", color = colors.textPrimary, style = LoungeType.title)
                Text(
                    text = if (store.boosted) "You're showing at the top" else "Get VIP · show on top for everyone",
                    color = colors.textSecondary,
                    style = LoungeType.caption,
                )
            }
            PrimaryButton(text = if (store.boosted) "On" else "Boost", onClick = { boostOpen = true }, enabled = !store.boosted)
        }
        Spacer(Modifier.height(LoungeSpace.s16))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(LoungeShape.card)
                .background(colors.surface)
                .border(1.dp, colors.border, LoungeShape.card),
        ) {
            SettingsRow(AppIcons.User, "Edit Profile", subtitle = "Name, country, gender, photo", onClick = onEdit, showDivider = true)
            SettingsRow(AppIcons.Crown, "Boost Profile", subtitle = "VIP badge, appear first", onClick = { boostOpen = true }, showDivider = true)
            SettingsRow(AppIcons.Coin, "My Coins", subtitle = "Buy coins", onClick = onCoins, showDivider = true)
            SettingsRow(AppIcons.Users, "My Friends", onClick = onFriends, showDivider = true)
            SettingsRow(
                AppIcons.Bell,
                "Notifications",
                trailing = SettingsTrailing.Toggle(store.notificationsOn) { store.notificationsOn = it },
                showDivider = true,
            )
            SettingsRow(
                AppIcons.Volume,
                "Chat sounds",
                trailing = SettingsTrailing.Toggle(store.chatSounds) { store.chatSounds = it },
                showDivider = true,
            )
            SettingsRow(AppIcons.Ban, "Account & Privacy", subtitle = "Blocked users, policy, delete account", onClick = onPrivacy, showDivider = true)
            SettingsRow(AppIcons.Flag, "Help & Support", onClick = onHelp)
        }
        Spacer(Modifier.height(LoungeSpace.s16))
    }

    ModalDialog(
        visible = boostOpen && !store.boosted,
        title = "Boost your profile?",
        body = "VIP puts you at the top of Discover and hides the banner ad on this device.",
        confirmLabel = "Boost",
        onConfirm = {
            store.boosted = true
            boostOpen = false
        },
        onDismiss = { boostOpen = false },
    )
}

@Composable
private fun InfoChip(text: String) {
    val colors = LoungeTheme.colors
    Text(
        text = text,
        color = colors.textPrimary,
        style = LoungeType.caption,
        modifier = Modifier
            .clip(LoungeShape.chip)
            .background(colors.surface)
            .border(1.dp, colors.border, LoungeShape.chip)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

@Composable
private fun PhotoSlot(
    photo: ProfilePhoto?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val colors = LoungeTheme.colors
    Box(
        modifier = modifier
            .height(108.dp)
            .clip(LoungeShape.input)
            .background(colors.surface)
            .clickable(onClick = onClick)
            .drawDashed(colors.outline),
        contentAlignment = Alignment.Center,
    ) {
        if (photo != null) {
            AsyncImage(
                model = photo.uri,
                contentDescription = "Profile photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            if (photo.status == PhotoStatus.Pending) {
                Text(
                    text = "Pending review",
                    color = colors.textPrimary,
                    style = LoungeType.caption,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(6.dp)
                        .clip(LoungeShape.chip)
                        .background(colors.background.copy(alpha = 0.86f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        } else {
            Icon(AppIcons.Camera, contentDescription = "Add photo", tint = colors.textSecondary, modifier = Modifier.size(22.dp))
            Icon(
                AppIcons.Plus,
                contentDescription = null,
                tint = colors.textSecondary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(14.dp),
            )
        }
    }
}

private fun Modifier.drawDashed(color: Color): Modifier = this.drawWithContent {
    drawContent()
    val stroke = 1.dp.toPx()
    drawRoundRect(
        color = color,
        style = Stroke(width = stroke, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx()),
    )
}
