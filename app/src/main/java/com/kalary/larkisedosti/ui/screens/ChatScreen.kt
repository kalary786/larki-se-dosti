package com.kalary.larkisedosti.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalary.larkisedosti.data.ChatMessage
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MessageKind
import com.kalary.larkisedosti.data.MockData
import com.kalary.larkisedosti.data.Person
import com.kalary.larkisedosti.ui.components.Avatar
import com.kalary.larkisedosti.ui.components.GiftArt
import com.kalary.larkisedosti.ui.components.IconCircleButton
import com.kalary.larkisedosti.ui.components.MenuEntry
import com.kalary.larkisedosti.ui.components.MenuSheet
import com.kalary.larkisedosti.ui.components.SendGiftSheet
import com.kalary.larkisedosti.ui.playBlip
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets
import com.kalary.larkisedosti.ui.theme.tag
import kotlinx.coroutines.delay

private val EmojiChoices = listOf("😊", "😂", "❤️", "🔥", "👋", "✨", "🎉", "💫")

@Composable
fun ChatScreen(
    personId: String,
    onBack: () -> Unit,
    onVoice: () -> Unit,
    onVideo: () -> Unit,
    onGift: (com.kalary.larkisedosti.data.Gift) -> Unit,
    onCoins: () -> Unit,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val person = app.person(personId)
    var draft by remember { mutableStateOf("") }
    var emojiOpen by remember { mutableStateOf(false) }
    var gifts by remember { mutableStateOf(false) }
    var menu by remember { mutableStateOf(false) }
    var sendTick by remember { mutableIntStateOf(0) }
    LaunchedEffect(personId) {
        app.markChatRead(personId)
        app.typingFor = personId
        delay(1400)
        if (app.typingFor == personId) app.typingFor = null
    }
    LaunchedEffect(sendTick) {
        if (sendTick == 0) return@LaunchedEffect
        app.typingFor = personId
        delay(900)
        app.reply(personId)
        if (app.typingFor == personId) app.typingFor = null
    }
    if (person == null) return
    val chat = app.conversation(personId)
    val muted = chat?.muted == true
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets()
            .tag("screen_chat"),
    ) {
        ChatHeader(person, muted, onBack, onVoice, onVideo, { app.conversation(personId)?.let { it.muted = !it.muted } }, { menu = true })
        Box(Modifier.weight(1f).fillMaxWidth()) {
            ParticleField(Modifier.fillMaxSize())
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier.fillMaxSize().padding(horizontal = Space.s16),
                verticalArrangement = Arrangement.spacedBy(Space.s8),
            ) {
                if (app.typingFor == app.baseId(personId)) {
                    item { TypingRow(person) }
                }
                val messages = chat?.messages?.toList().orEmpty().asReversed()
                items(messages, key = { it.id }) { message ->
                    Bubble(message, person)
                }
                item {
                    Text(
                        "Today",
                        style = AppType.caption,
                        color = colors.textSecondary,
                        modifier = Modifier
                            .padding(vertical = Space.s8)
                            .clip(RoundedCornerShape(Radius.chip))
                            .background(colors.surfaceElevated)
                            .padding(horizontal = Space.s12, vertical = Space.s4)
                            .fillMaxWidthWrap(),
                    )
                }
            }
        }
        if (emojiOpen) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = Space.s16, vertical = Space.s8),
                horizontalArrangement = Arrangement.spacedBy(Space.s8),
            ) {
                EmojiChoices.forEach { emoji ->
                    Text(
                        emoji,
                        fontSize = 22.sp,
                        modifier = Modifier.clickable { draft += emoji },
                    )
                }
            }
        }
        ChatInput(
            draft = draft,
            onDraft = { draft = it },
            onEmoji = { emojiOpen = !emojiOpen },
            onGift = { gifts = true },
            onSend = {
                val sending = draft
                app.sendText(personId, sending)
                if (app.chatSounds && app.conversation(personId)?.muted != true) playBlip()
                draft = ""
                sendTick += 1
            },
        )
    }
    if (menu && person != null) {
        MenuSheet(
            title = person.name,
            entries = listOf(
                MenuEntry(Lucide.Gift, "Send gift") { gifts = true },
                MenuEntry(Lucide.Flag, "Report") { app.reportFor = person.id },
                MenuEntry(Lucide.Ban, "Block", danger = true) { app.blockFor = person.id },
                MenuEntry(Lucide.Trash, "Delete chat", danger = true) { app.deleteChatFor = person.id },
            ),
            onDismiss = { menu = false },
        )
    }
    if (gifts && person != null) {
        SendGiftSheet(
            name = person.name,
            balance = app.coins,
            onClose = { gifts = false },
            onBuyCoins = { gifts = false; onCoins() },
            onSend = { gift ->
                gifts = false
                onGift(gift)
            },
        )
    }
}

@Composable
private fun ChatHeader(
    person: Person,
    muted: Boolean,
    onBack: () -> Unit,
    onVoice: () -> Unit,
    onVideo: () -> Unit,
    onMute: () -> Unit,
    onMore: () -> Unit,
) {
    val colors = LarkiTheme.colors
    Row(
        Modifier.fillMaxWidth().padding(horizontal = Space.s12, vertical = Space.s8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconCircleButton(Lucide.ArrowLeft, "Back", onBack, size = 36.dp, iconSize = 16.dp)
        Avatar(person, size = 40.dp, showOnline = true, modifier = Modifier.padding(start = Space.s8))
        Column(Modifier.weight(1f).padding(horizontal = Space.s8)) {
            Text(person.name, style = AppType.title, color = colors.textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = if (person.online) colors.online else colors.textSecondary)) {
                        append(if (person.online) "Online" else "Offline")
                    }
                    withStyle(SpanStyle(color = colors.textSecondary)) {
                        append(" · ${person.flag} ${person.country}")
                    }
                },
                style = AppType.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        IconCircleButton(Lucide.Phone, "Voice call", onVoice, tint = colors.online, size = 36.dp, iconSize = 16.dp)
        IconCircleButton(Lucide.Video, "Video call", onVideo, tint = colors.primary, size = 36.dp, iconSize = 16.dp, modifier = Modifier.padding(start = Space.s4))
        IconCircleButton(
            if (muted) Lucide.VolumeX else Lucide.Volume2,
            "Mute",
            onMute,
            tint = if (muted) colors.primary else colors.textSecondary,
            size = 36.dp,
            iconSize = 16.dp,
            modifier = Modifier.padding(start = Space.s4),
        )
        IconCircleButton(Lucide.EllipsisVertical, "More", onMore, size = 36.dp, iconSize = 16.dp, modifier = Modifier.padding(start = Space.s4))
    }
}

@Composable
private fun Bubble(message: ChatMessage, person: Person) {
    val colors = LarkiTheme.colors
    val mine = message.mine
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = if (mine) Alignment.End else Alignment.Start,
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (!mine) {
                Avatar(person, size = 28.dp, modifier = Modifier.padding(end = Space.s8))
            }
            if (message.kind == MessageKind.Gift) {
                val gift = MockData.gifts.firstOrNull { it.id == message.giftId }
                Column(
                    Modifier
                        .clip(RoundedCornerShape(Radius.card))
                        .background(colors.surfaceElevated)
                        .padding(Space.s12),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (gift != null) GiftArt(gift.kind, Modifier.size(48.dp))
                    Text(message.text, style = AppType.title, color = colors.textPrimary)
                    Text("Gift", style = AppType.caption, color = colors.textSecondary)
                }
            } else {
                val shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (mine) 18.dp else 6.dp,
                    bottomEnd = if (mine) 6.dp else 18.dp,
                )
                Text(
                    message.text,
                    style = if (message.kind == MessageKind.Emoji) AppType.h2.copy(fontSize = 28.sp) else AppType.body,
                    color = if (mine) colors.onPrimary else colors.textPrimary,
                    modifier = Modifier
                        .clip(shape)
                        .background(if (mine) colors.primary else colors.surface)
                        .padding(horizontal = Space.s12, vertical = Space.s8),
                )
            }
        }
        Text(
            message.time,
            style = AppType.caption,
            color = colors.textSecondary,
            modifier = Modifier.padding(top = Space.s4, start = if (mine) 0.dp else 36.dp),
        )
    }
}

@Composable
private fun TypingRow(person: Person) {
    val colors = LarkiTheme.colors
    Row(verticalAlignment = Alignment.CenterVertically) {
        Avatar(person, size = 28.dp)
        Row(
            Modifier
                .padding(start = Space.s8)
                .clip(RoundedCornerShape(18.dp))
                .background(colors.surface)
                .padding(horizontal = Space.s12, vertical = Space.s8),
            horizontalArrangement = Arrangement.spacedBy(Space.s4),
        ) {
            val transition = rememberInfiniteTransition(label = "typing")
            repeat(3) { index ->
                val alpha by transition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        tween(420),
                        RepeatMode.Reverse,
                        initialStartOffset = StartOffset(index * 120),
                    ),
                    label = "dot$index",
                )
                Box(
                    Modifier
                        .size(7.dp)
                        .graphicsLayer { this.alpha = alpha }
                        .clip(CircleShape)
                        .background(colors.textSecondary),
                )
            }
        }
    }
}

@Composable
private fun ChatInput(
    draft: String,
    onDraft: (String) -> Unit,
    onEmoji: () -> Unit,
    onGift: () -> Unit,
    onSend: () -> Unit,
) {
    val colors = LarkiTheme.colors
    Row(
        Modifier
            .fillMaxWidth()
            .padding(Space.s12)
            .clip(RoundedCornerShape(Radius.chip))
            .background(colors.surfaceElevated)
            .padding(horizontal = Space.s8, vertical = Space.s8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconCircleButton(Lucide.Smile, "Emoji", onEmoji, size = 36.dp, iconSize = 16.dp)
        Box(
            Modifier
                .padding(horizontal = Space.s8)
                .width(1.dp)
                .size(width = 1.dp, height = 22.dp)
                .background(colors.border),
        )
        BasicTextField(
            value = draft,
            onValueChange = onDraft,
            textStyle = AppType.body.copy(color = colors.textPrimary),
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                if (draft.isEmpty()) Text("Type a message…", style = AppType.body, color = colors.textSecondary)
                inner()
            },
        )
        IconCircleButton(Lucide.Gift, "Gift", onGift, size = 36.dp, iconSize = 16.dp)
        Box(
            Modifier
                .padding(start = Space.s4)
                .size(36.dp)
                .clip(CircleShape)
                .background(if (draft.isBlank()) colors.border else colors.primary)
                .clickable(enabled = draft.isNotBlank(), onClick = onSend),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Lucide.Send, contentDescription = "Send", tint = colors.onPrimary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun ParticleField(modifier: Modifier) {
    val colors = LarkiTheme.colors
    val transition = rememberInfiniteTransition(label = "particles")
    val time by transition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(tween(9000, easing = LinearEasing)),
        label = "time",
    )
    Canvas(modifier) {
        val spec = listOf(
            Triple(0.15f, 0.2f, 0.08f),
            Triple(0.72f, 0.35f, 0.05f),
            Triple(0.4f, 0.7f, 0.06f),
            Triple(0.85f, 0.8f, 0.04f),
            Triple(0.25f, 0.9f, 0.07f),
            Triple(0.6f, 0.15f, 0.05f),
        )
        spec.forEachIndexed { index, (x, y, r) ->
            val drift = (y - time * (0.15f + index * 0.03f)).mod(1f)
            drawCircle(
                color = colors.primary.copy(alpha = 0.12f),
                radius = size.minDimension * r,
                center = Offset(x * size.width, drift * size.height),
            )
        }
    }
}

private fun Modifier.fillMaxWidthWrap(): Modifier = this
