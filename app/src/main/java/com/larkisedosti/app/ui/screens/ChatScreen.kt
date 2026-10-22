package com.larkisedosti.app.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.ChatMessage
import com.larkisedosti.app.model.GiftKind
import com.larkisedosti.app.model.formatChatDay
import com.larkisedosti.app.model.formatClock
import com.larkisedosti.app.ui.components.Avatar
import com.larkisedosti.app.ui.components.BlockUserDialog
import com.larkisedosti.app.ui.components.DeleteChatDialog
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.LoungeMenu
import com.larkisedosti.app.ui.components.MenuEntry
import com.larkisedosti.app.ui.components.NotEnoughCoinsModal
import com.larkisedosti.app.ui.components.ReportUserDialog
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import kotlinx.coroutines.delay

@Composable
fun ChatScreen(
    store: AppStore,
    personId: String,
    onBack: () -> Unit,
    onVoice: () -> Unit,
    onVideo: () -> Unit,
    onCoins: () -> Unit,
) {
    val person = store.person(personId)
    LaunchedEffect(personId) { store.markRead(personId) }
    if (person == null) {
        LaunchedEffect(Unit) { onBack() }
        return
    }
    val colors = LoungeTheme.colors
    val conversation = store.openConversation(personId)
    var draft by remember { mutableStateOf("") }
    var emojiOpen by remember { mutableStateOf(false) }
    var menu by remember { mutableStateOf(false) }
    var muted by remember { mutableStateOf(false) }
    var giftOpen by remember { mutableStateOf(false) }
    var pendingGift by remember { mutableStateOf<GiftKind?>(null) }
    var needCoins by remember { mutableStateOf(false) }
    var burst by remember { mutableStateOf<String?>(null) }
    var report by remember { mutableStateOf(false) }
    var block by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }
    var typing by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val sendDraft = {
        val text = draft.trim()
        if (text.isNotEmpty()) {
            store.sendText(person.id, text)
            draft = ""
            if (person.online) typing = true
        }
    }
    LaunchedEffect(typing) {
        if (typing) {
            delay(1600)
            typing = false
        }
    }
    val rows = chatRows(conversation.messages)
    LaunchedEffect(rows.size, typing) {
        val index = (rows.lastIndex + if (typing) 1 else 0).coerceAtLeast(0)
        if (rows.isNotEmpty() || typing) listState.animateScrollToItem(index)
    }
    val drift = rememberInfiniteTransition(label = "particles")
    val phase = drift.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing)),
        label = "phase",
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            repeat(14) { index ->
                val x = ((index * 83 + phase.value * size.width) % size.width)
                val y = ((index * 47f + phase.value * size.height * 0.35f) % size.height)
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = 1.6f + (index % 3) * 0.7f,
                    center = Offset(x, y),
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.background),
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = LoungeSpace.s8, vertical = LoungeSpace.s8),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconCircleButton(
                    icon = AppIcons.ChevronLeft,
                    contentDescription = "Back",
                    onClick = onBack,
                    size = 40.dp,
                )
                Spacer(Modifier.width(6.dp))
                Avatar(
                    name = person.name,
                    size = 36.dp,
                    online = person.online,
                    containerColor = person.color,
                    dotRing = colors.background,
                )
                Spacer(Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = person.name,
                        color = colors.textPrimary,
                        style = LoungeType.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = when {
                            typing -> "Typing..."
                            person.online -> "Online"
                            else -> "Offline"
                        },
                        color = if (typing || person.online) colors.online else colors.textSecondary,
                        style = LoungeType.caption,
                    )
                    Text(
                        text = "${person.countryFlag}  ${person.countryName}",
                        color = colors.textSecondary,
                        style = LoungeType.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                IconCircleButton(
                    icon = AppIcons.Phone,
                    contentDescription = "Voice call",
                    onClick = onVoice,
                    size = 36.dp,
                    tint = colors.online,
                )
                Spacer(Modifier.width(4.dp))
                IconCircleButton(
                    icon = AppIcons.Video,
                    contentDescription = "Video call",
                    onClick = onVideo,
                    size = 36.dp,
                    tint = colors.primary,
                )
                Spacer(Modifier.width(4.dp))
                IconCircleButton(
                    icon = if (muted) AppIcons.BellOff else AppIcons.Bell,
                    contentDescription = if (muted) "Unmute notifications" else "Mute notifications",
                    onClick = { muted = !muted },
                    size = 36.dp,
                )
                Spacer(Modifier.width(4.dp))
                Box {
                    IconCircleButton(
                        icon = AppIcons.MoreVertical,
                        contentDescription = "More",
                        onClick = { menu = true },
                        size = 36.dp,
                    )
                    LoungeMenu(
                        expanded = menu,
                        onDismiss = { menu = false },
                        entries = listOf(
                            MenuEntry("Send gift", AppIcons.Gift) { giftOpen = true },
                            MenuEntry("Report", AppIcons.Flag) { report = true },
                            MenuEntry("Block", AppIcons.Ban, danger = true) { block = true },
                            MenuEntry("Delete chat", AppIcons.Trash, danger = true) { delete = true },
                        ),
                    )
                }
            }
            HorizontalDivider(color = colors.border, thickness = 1.dp)
            }
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = LoungeSpace.s16),
                contentPadding = PaddingValues(top = LoungeSpace.s20, bottom = LoungeSpace.s8),
                verticalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
            ) {
                items(rows, key = { it.key }) { row ->
                    when (row) {
                        is ChatRow.Day -> DayChip(row.label)
                        is ChatRow.Text -> Bubble(
                            message = row.message,
                            name = person.name,
                            color = person.color,
                            online = person.online,
                            showAvatar = row.showAvatar,
                        )
                        is ChatRow.Gift -> GiftMessage(row.message, person.name)
                    }
                }
                if (typing) {
                    item(key = "typing") {
                        TypingRow(name = person.name, color = person.color, online = person.online)
                    }
                }
            }
            if (emojiOpen) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = LoungeSpace.s16, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    listOf("\uD83D\uDE0A", "\uD83D\uDE02", "\u2764\uFE0F", "\uD83D\uDD25", "\uD83D\uDC4D", "\uD83D\uDE4F").forEach { emoji ->
                        Text(
                            text = emoji,
                            style = LoungeType.title,
                            modifier = Modifier.clickable { draft += emoji },
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = LoungeSpace.s12, vertical = LoungeSpace.s8)
                    .fillMaxWidth()
                    .clip(LoungeShape.chip)
                    .background(colors.surface)
                    .border(1.dp, colors.border, LoungeShape.chip)
                    .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconCircleButton(
                    icon = AppIcons.Smile,
                    contentDescription = "Emoji",
                    onClick = { emojiOpen = !emojiOpen },
                    size = 36.dp,
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .width(1.dp)
                        .height(22.dp)
                        .background(colors.border),
                )
                BasicTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    textStyle = LoungeType.body.copy(color = colors.textPrimary),
                    cursorBrush = SolidColor(colors.primary),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { sendDraft() }),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    decorationBox = { inner ->
                        Box {
                            if (draft.isEmpty()) {
                                Text(
                                    text = "Type a message...",
                                    color = colors.textSecondary,
                                    style = LoungeType.body,
                                )
                            }
                            inner()
                        }
                    },
                )
                IconCircleButton(
                    icon = AppIcons.Gift,
                    contentDescription = "Gift",
                    onClick = { giftOpen = true },
                    size = 36.dp,
                )
                Spacer(Modifier.width(4.dp))
                IconCircleButton(
                    icon = AppIcons.Send,
                    contentDescription = "Send",
                    onClick = { if (draft.isNotBlank()) sendDraft() },
                    size = 40.dp,
                    tint = if (draft.isNotBlank()) colors.onPrimary else colors.textSecondary,
                    containerColor = if (draft.isNotBlank()) colors.primary else colors.surface,
                )
            }
        }
        burst?.let { title -> GiftBurst(title = title, onFinished = { burst = null }) }
    }

    SendGiftSheet(
        visible = giftOpen,
        recipient = person.name,
        balance = store.coins,
        onClose = { giftOpen = false },
        onAddCoins = {
            giftOpen = false
            onCoins()
        },
        onSend = { gift ->
            if (store.sendGift(person.id, gift)) {
                giftOpen = false
                burst = gift.title
            } else {
                pendingGift = gift
                needCoins = true
            }
        },
    )
    NotEnoughCoinsModal(
        visible = needCoins,
        balance = store.coins,
        needed = pendingGift?.price ?: AppStore.CallCost,
        forCall = false,
        onBuyCoins = {
            needCoins = false
            onCoins()
        },
        onFreeTry = {
            val gift = pendingGift
            needCoins = false
            if (gift != null && store.sendGift(person.id, gift, free = true)) {
                giftOpen = false
                burst = gift.title
            }
        },
        onDismiss = { needCoins = false },
    )
    ReportUserDialog(
        visible = report,
        name = person.name,
        onDismiss = { report = false },
        onSubmit = { reason, note ->
            store.fileReport(person.id, reason, note)
            report = false
        },
    )
    BlockUserDialog(
        visible = block,
        name = person.name,
        onDismiss = { block = false },
        onConfirm = {
            store.block(person.id)
            block = false
            onBack()
        },
    )
    DeleteChatDialog(
        visible = delete,
        name = person.name,
        onDismiss = { delete = false },
        onConfirm = {
            store.conversations.removeAll { it.personId == person.id }
            delete = false
            onBack()
        },
    )
}

@Composable
private fun TypingRow(
    name: String,
    color: Color,
    online: Boolean,
) {
    val colors = LoungeTheme.colors
    val transition = rememberInfiniteTransition(label = "typing")
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {
        Avatar(
            name = name,
            size = 28.dp,
            online = online,
            containerColor = color,
            dotRing = colors.background,
        )
        Spacer(Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(colors.surface)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(3) { index ->
                val jump by transition.animateFloat(
                    initialValue = 0f,
                    targetValue = -5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 280, delayMillis = index * 120, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ),
                    label = "dot$index",
                )
                Box(
                    modifier = Modifier
                        .offset(y = jump.dp)
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(colors.textSecondary),
                )
            }
        }
    }
}

@Composable
private fun DayChip(label: String) {
    val colors = LoungeTheme.colors
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = label,
            color = colors.textSecondary,
            style = LoungeType.caption,
            modifier = Modifier
                .clip(LoungeShape.chip)
                .background(colors.surface)
                .padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun Bubble(
    message: ChatMessage,
    name: String,
    color: Color,
    online: Boolean,
    showAvatar: Boolean,
) {
    val colors = LoungeTheme.colors
    val mine = message.fromMe
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (mine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom,
    ) {
        if (!mine) {
            if (showAvatar) {
                Avatar(
                    name = name,
                    size = 28.dp,
                    online = online,
                    containerColor = color,
                    dotRing = colors.background,
                )
            } else {
                Spacer(Modifier.size(28.dp))
            }
            Spacer(Modifier.width(8.dp))
        }
        Column(horizontalAlignment = if (mine) Alignment.End else Alignment.Start) {
            Text(
                text = message.text,
                color = if (mine) colors.onPrimary else colors.textPrimary,
                style = LoungeType.body,
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (mine) colors.primary else colors.surface)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            )
            Spacer(Modifier.height(4.dp))
            Text(text = formatClock(message.sentAt), color = colors.textSecondary, style = LoungeType.caption)
        }
    }
}

@Composable
private fun GiftMessage(
    message: ChatMessage,
    name: String,
) {
    val gift = message.gift ?: return
    val colors = LoungeTheme.colors
    val pulse = rememberInfiniteTransition(label = "gift-${message.id}")
    val scale by pulse.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "gift-scale",
    )
    val line = if (message.fromMe) "You sent a ${gift.title}" else "$name sent you a ${gift.title}"
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 240.dp)
                .clip(LoungeShape.card)
                .background(colors.surfaceElevated)
                .border(1.dp, colors.border, LoungeShape.card)
                .padding(horizontal = LoungeSpace.s16, vertical = LoungeSpace.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = gift.emoji(),
                fontSize = 36.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            )
            Spacer(Modifier.height(LoungeSpace.s8))
            Text(
                text = line,
                color = colors.textPrimary,
                style = LoungeType.title,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${gift.price} coins",
                color = colors.coin,
                style = LoungeType.caption,
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(text = formatClock(message.sentAt), color = colors.textSecondary, style = LoungeType.caption)
    }
}

private sealed interface ChatRow {
    val key: String

    data class Day(val label: String, val anchor: String) : ChatRow {
        override val key: String = "day-$anchor"
    }

    data class Text(val message: ChatMessage, val showAvatar: Boolean) : ChatRow {
        override val key: String = message.id
    }

    data class Gift(val message: ChatMessage) : ChatRow {
        override val key: String = message.id
    }
}

private fun chatRows(messages: List<ChatMessage>): List<ChatRow> {
    return messages.mapIndexed { index, message ->
        val day = formatChatDay(message.sentAt)
        val showDay = index == 0 || formatChatDay(messages[index - 1].sentAt) != day
        val next = messages.getOrNull(index + 1)
        val showAvatar = !message.fromMe && (
            next == null || next.fromMe || next.gift != null || formatChatDay(next.sentAt) != day
            )
        Triple(showDay, day, message to showAvatar)
    }.flatMap { (showDay, day, pair) ->
        val (message, showAvatar) = pair
        buildList {
            if (showDay) add(ChatRow.Day(day, message.id))
            if (message.gift != null) add(ChatRow.Gift(message)) else add(ChatRow.Text(message, showAvatar))
        }
    }
}

private fun GiftKind.emoji(): String = when (this) {
    GiftKind.Rose -> "\uD83C\uDF39"
    GiftKind.Heart -> "\u2764\uFE0F"
    GiftKind.Star -> "\u2B50"
    GiftKind.Bouquet -> "\uD83D\uDC90"
    GiftKind.Diamond -> "\uD83D\uDC8E"
    GiftKind.Crown -> "\uD83D\uDC51"
}
