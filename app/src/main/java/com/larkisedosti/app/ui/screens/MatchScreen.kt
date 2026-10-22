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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.Gender
import com.larkisedosti.app.model.Person
import com.larkisedosti.app.ui.components.Avatar
import com.larkisedosti.app.ui.components.GhostButton
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.PrimaryButton
import com.larkisedosti.app.ui.components.SegmentedControl
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.shell.LocalShellInset
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun MatchScreen(
    store: AppStore,
    onOpenChat: (String) -> Unit,
    onCall: (String) -> Unit,
) {
    val colors = LoungeTheme.colors
    var segment by remember { mutableStateOf("All") }
    var soundOn by remember { mutableStateOf(true) }
    var selectedId by remember { mutableStateOf<String?>(null) }
    val people = store.directory.filter { person ->
        person.id !in store.blockedIds && when (segment) {
            "Girls" -> person.gender == Gender.Female
            "Boys" -> person.gender == Gender.Male
            else -> true
        }
    }.take(8)
    val selected = people.find { it.id == selectedId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = LoungeSpace.s16, vertical = LoungeSpace.s12)
            .padding(bottom = LocalShellInset.current),
    ) {
        PrimaryButton(
            text = "Instant Live Match",
            onClick = { selectedId = people.randomOrNull()?.id },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(LoungeSpace.s12))
        SegmentedControl(
            options = listOf("All", "Girls", "Boys"),
            selected = segment,
            onSelect = {
                segment = it
                selectedId = null
            },
        )
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(
            text = "Near ${store.country.city}, ${store.country.region} · plus other countries · tap a person",
            color = colors.textSecondary,
            style = LoungeType.caption,
        )
        Spacer(Modifier.height(LoungeSpace.s12))
        Radar(
            people = people,
            selected = selected,
            soundOn = soundOn,
            onToggleSound = { soundOn = !soundOn },
            onSelect = { selectedId = it },
            onClear = { selectedId = null },
            onChat = { id -> onOpenChat(id) },
            onCall = { id -> onCall(id) },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun Radar(
    people: List<Person>,
    selected: Person?,
    soundOn: Boolean,
    onToggleSound: () -> Unit,
    onSelect: (String) -> Unit,
    onClear: () -> Unit,
    onChat: (String) -> Unit,
    onCall: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LoungeTheme.colors
    val transition = rememberInfiniteTransition(label = "radar")
    val angle = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep",
    )
    val pulse = transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.16f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse",
    )
    val labels = listOf("Near", "City", "State", "World")
    val clearInteraction = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .clip(LoungeShape.card)
            .background(colors.surface)
            .border(1.dp, colors.border, LoungeShape.card)
            .clickable(
                interactionSource = clearInteraction,
                indication = null,
                onClick = onClear,
            ),
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val widthPx = with(density) { maxWidth.toPx() }
            val heightPx = with(density) { maxHeight.toPx() }
            val center = Offset(widthPx / 2f, heightPx / 2f)
            val maxRadius = minOf(widthPx, heightPx) / 2f - with(density) { 28.dp.toPx() }
            Canvas(modifier = Modifier.fillMaxSize()) {
                val accent = colors.primary
                labels.indices.forEach { index ->
                    val radius = maxRadius * (0.28f + index * 0.22f)
                    drawCircle(
                        color = colors.border,
                        radius = radius,
                        center = center,
                        style = Stroke(width = 1.dp.toPx()),
                    )
                }
                rotate(angle.value, center) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            0f to Color.Transparent,
                            0.12f to accent.copy(alpha = 0.28f),
                            0.22f to Color.Transparent,
                            1f to Color.Transparent,
                            center = center,
                        ),
                        radius = maxRadius,
                        center = center,
                    )
                    drawLine(
                        color = accent.copy(alpha = 0.9f),
                        start = center,
                        end = Offset(center.x + maxRadius, center.y),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                    )
                }
            }
            labels.forEachIndexed { index, label ->
                val radius = maxRadius * (0.28f + index * 0.22f)
                Text(
                    text = label,
                    color = colors.textSecondary,
                    style = LoungeType.caption,
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                center.x.roundToInt(),
                                (center.y - radius).roundToInt(),
                            )
                        }
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            layout(placeable.width, placeable.height) {
                                placeable.place(-placeable.width / 2, -placeable.height / 2)
                            }
                        }
                        .clip(LoungeShape.chip)
                        .background(colors.surfaceElevated)
                        .border(1.dp, colors.border, LoungeShape.chip)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }
            people.groupBy { it.ring.coerceIn(0, 3) }.forEach { (ring, group) ->
                val radius = maxRadius * (0.28f + ring * 0.22f)
                val lane = 32.0
                val start = 270.0 + lane
                val sweep = 360.0 - lane * 2
                group.forEachIndexed { index, person ->
                    val angle = start + sweep * (index + 0.5) / group.size
                    val radians = Math.toRadians(angle)
                    val x = center.x + cos(radians) * radius
                    val y = center.y + sin(radians) * radius
                    Avatar(
                        name = person.name,
                        size = 36.dp,
                        online = person.online,
                        containerColor = person.color,
                        dotRing = colors.surface,
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    (x - 18.dp.toPx()).roundToInt(),
                                    (y - 18.dp.toPx()).roundToInt(),
                                )
                            }
                            .clickable { onSelect(person.id) },
                    )
                }
            }
            Icon(
                imageVector = AppIcons.Heart,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(28.dp)
                    .graphicsLayer {
                        scaleX = pulse.value
                        scaleY = pulse.value
                    },
            )
        }
        IconCircleButton(
            icon = if (soundOn) AppIcons.Volume else AppIcons.VolumeOff,
            contentDescription = if (soundOn) "Sound on" else "Sound off",
            onClick = onToggleSound,
            size = 40.dp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
        )
        if (selected != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clip(LoungeShape.card)
                    .background(colors.surfaceElevated)
                    .padding(LoungeSpace.s12),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(
                        name = selected.name,
                        size = 40.dp,
                        online = selected.online,
                        containerColor = selected.color,
                        dotRing = colors.surfaceElevated,
                    )
                    Spacer(Modifier.size(LoungeSpace.s12))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = selected.name, color = colors.textPrimary, style = LoungeType.title)
                        Text(
                            text = "${selected.city}, ${selected.countryName}",
                            color = colors.textSecondary,
                            style = LoungeType.caption,
                        )
                    }
                }
                Spacer(Modifier.height(LoungeSpace.s12))
                Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
                    GhostButton(
                        text = "Chat",
                        onClick = { onChat(selected.id) },
                        modifier = Modifier.weight(1f),
                    )
                    PrimaryButton(
                        text = "Call",
                        onClick = { onCall(selected.id) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
