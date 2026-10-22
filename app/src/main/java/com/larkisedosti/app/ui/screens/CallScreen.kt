package com.larkisedosti.app.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.Person
import com.larkisedosti.app.ui.components.Avatar
import com.larkisedosti.app.ui.components.BlockUserDialog
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.LoungeMenu
import com.larkisedosti.app.ui.components.MenuEntry
import com.larkisedosti.app.ui.components.NotEnoughCoinsModal
import com.larkisedosti.app.ui.components.PrimaryButton
import com.larkisedosti.app.ui.components.ReportUserDialog
import com.larkisedosti.app.ui.components.SecondaryButton
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun CallScreen(
    store: AppStore,
    person: Person,
    video: Boolean,
    incoming: Boolean,
    waived: Boolean = false,
    onBack: () -> Unit,
    onCoins: () -> Unit,
) {
    val colors = LoungeTheme.colors
    var phase by remember { mutableStateOf(if (incoming) "incoming" else "calling") }
    var seconds by remember { mutableIntStateOf(0) }
    var showTimer by remember { mutableStateOf(false) }
    var dialLabel by remember { mutableStateOf(if (incoming) "Ringing..." else "Calling...") }
    var muted by remember { mutableStateOf(false) }
    var speaker by remember { mutableStateOf(false) }
    var flipped by remember { mutableStateOf(false) }
    var needCoins by remember { mutableStateOf(false) }
    var waivedCall by remember { mutableStateOf(waived) }
    var menu by remember { mutableStateOf(false) }
    var report by remember { mutableStateOf(false) }
    var block by remember { mutableStateOf(false) }
    var previewOffset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(phase, waivedCall) {
        if (phase == "calling") {
            dialLabel = "Calling..."
            delay(700)
            if (phase != "calling") return@LaunchedEffect
            dialLabel = "Ringing..."
            delay(900)
            if (phase != "calling") return@LaunchedEffect
            if (waivedCall || store.chargeCall()) phase = "live" else needCoins = true
        }
    }
    LaunchedEffect(phase) {
        if (phase != "live") return@LaunchedEffect
        dialLabel = "Connected"
        delay(800)
        showTimer = true
        while (true) {
            delay(1000)
            seconds += 1
        }
    }

    val status = if (phase == "live" && showTimer) {
        "%02d:%02d".format(seconds / 60, seconds % 60)
    } else {
        dialLabel
    }
    val coinLine = "${AppStore.CallCost} coins per minute · Balance: ${store.coins}"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        if (video) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.fillMaxSize().background(person.color))
                person.photoUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = person.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
                if (person.photoUrl == null) {
                    Avatar(name = person.name, size = 120.dp, containerColor = person.color)
                }
            }
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 64.dp, end = LoungeSpace.s16)
                    .align(Alignment.TopEnd)
                    .offset { IntOffset(previewOffset.x.roundToInt(), previewOffset.y.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, drag ->
                            change.consume()
                            previewOffset += drag
                        }
                    }
                    .size(width = 100.dp, height = 150.dp)
                    .graphicsLayer { scaleX = if (flipped) -1f else 1f }
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.surfaceElevated),
                contentAlignment = Alignment.Center,
            ) {
                Avatar(name = "You", size = 48.dp, containerColor = colors.surface)
                Text(
                    text = "You",
                    color = colors.textSecondary,
                    style = LoungeType.caption,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.background.copy(alpha = 0.72f))
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }
        } else {
            VoiceCenter(
                person = person,
                status = status,
                coinLine = coinLine,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = LoungeSpace.s12, vertical = LoungeSpace.s8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconCircleButton(
                icon = AppIcons.ChevronLeft,
                contentDescription = "Back",
                onClick = onBack,
                size = 40.dp,
            )
            Spacer(Modifier.weight(1f))
            Box {
                IconCircleButton(
                    icon = AppIcons.MoreVertical,
                    contentDescription = "More",
                    onClick = { menu = true },
                    size = 40.dp,
                )
                LoungeMenu(
                    expanded = menu,
                    onDismiss = { menu = false },
                    entries = listOf(
                        MenuEntry("Report", AppIcons.Flag) {
                            menu = false
                            report = true
                        },
                        MenuEntry("Block", AppIcons.Ban, danger = true) {
                            menu = false
                            block = true
                        },
                    ),
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xFF14112B).copy(alpha = if (video) 0.92f else 0f)),
                    ),
                )
                .navigationBarsPadding()
                .padding(horizontal = LoungeSpace.s20)
                .padding(top = LoungeSpace.s32, bottom = LoungeSpace.s16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (video) {
                Text(text = person.name, color = Color.White, style = LoungeType.h2)
                Spacer(Modifier.height(LoungeSpace.s8))
                Text(text = status, color = Color.White.copy(alpha = 0.86f), style = LoungeType.body)
                Spacer(Modifier.height(4.dp))
                Text(text = coinLine, color = Color.White.copy(alpha = 0.72f), style = LoungeType.caption)
                Spacer(Modifier.height(LoungeSpace.s20))
            }
            if (phase == "incoming") {
                Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s12)) {
                    SecondaryButton(text = "Decline", onClick = onBack)
                    PrimaryButton(
                        text = "Accept",
                        onClick = {
                            if (waivedCall || store.chargeCall()) phase = "live" else needCoins = true
                        },
                    )
                }
            } else {
                CallControls(
                    video = video,
                    muted = muted,
                    speaker = speaker,
                    onMute = { muted = !muted },
                    onSpeaker = { speaker = !speaker },
                    onFlip = { flipped = !flipped },
                    onEnd = onBack,
                )
            }
        }
    }
    NotEnoughCoinsModal(
        visible = needCoins,
        balance = store.coins,
        needed = AppStore.CallCost,
        forCall = true,
        onBuyCoins = {
            needCoins = false
            onCoins()
        },
        onFreeTry = {
            waivedCall = true
            needCoins = false
            phase = "live"
        },
        onDismiss = {
            needCoins = false
            onBack()
        },
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
}

@Composable
private fun VoiceCenter(
    person: Person,
    status: String,
    coinLine: String,
) {
    val colors = LoungeTheme.colors
    val pulse = rememberInfiniteTransition(label = "call-ring")
    val scale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.16f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "ring-scale",
    )
    val alpha by pulse.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.08f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "ring-alpha",
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 180.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(156.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .border(2.dp, colors.primary, CircleShape),
            )
            Avatar(
                name = person.name,
                size = 120.dp,
                online = person.online,
                containerColor = person.color,
            )
        }
        Spacer(Modifier.height(LoungeSpace.s20))
        Text(text = person.name, color = colors.textPrimary, style = LoungeType.h2)
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(text = status, color = colors.textSecondary, style = LoungeType.body)
        Spacer(Modifier.height(4.dp))
        Text(text = coinLine, color = colors.textSecondary, style = LoungeType.caption)
    }
}

@Composable
private fun CallControls(
    video: Boolean,
    muted: Boolean,
    speaker: Boolean,
    onMute: () -> Unit,
    onSpeaker: () -> Unit,
    onFlip: () -> Unit,
    onEnd: () -> Unit,
) {
    val colors = LoungeTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top,
    ) {
        ControlButton(
            icon = if (muted) AppIcons.MicOff else AppIcons.Mic,
            label = "Mute",
            onClick = onMute,
        )
        ControlButton(
            icon = if (speaker) AppIcons.Volume else AppIcons.VolumeOff,
            label = "Speaker",
            onClick = onSpeaker,
        )
        if (video) {
            ControlButton(
                icon = AppIcons.Camera,
                label = "Flip",
                onClick = onFlip,
            )
        }
    }
    Spacer(Modifier.height(LoungeSpace.s16))
    IconCircleButton(
        icon = AppIcons.Phone,
        contentDescription = "End call",
        onClick = onEnd,
        size = 72.dp,
        rotation = 135f,
        containerColor = colors.danger,
        tint = colors.onPrimary,
    )
}

@Composable
private fun ControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    val colors = LoungeTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconCircleButton(
            icon = icon,
            contentDescription = label,
            onClick = onClick,
            size = 48.dp,
        )
        Spacer(Modifier.height(6.dp))
        Text(text = label, color = colors.textSecondary, style = LoungeType.caption)
    }
}
