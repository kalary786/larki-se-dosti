package com.kalary.larkisedosti.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.Distance
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MatchFilter
import com.kalary.larkisedosti.data.Person
import com.kalary.larkisedosti.ui.components.Avatar
import com.kalary.larkisedosti.ui.components.FilterChip
import com.kalary.larkisedosti.ui.components.IconCircleButton
import com.kalary.larkisedosti.ui.components.PrimaryButton
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.tag
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun MatchScreen(
    onOpenChat: (String) -> Unit,
    onCall: (String, Boolean) -> Unit,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val context = LocalContext.current
    var selected by remember { mutableStateOf<Person?>(null) }
    val people = app.matchPeople()
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = Space.s16)
            .tag("screen_match"),
    ) {
        PrimaryButton(
            text = "Instant Live Match",
            onClick = { selected = people.randomOrNull() },
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            Modifier.padding(top = Space.s12),
            horizontalArrangement = Arrangement.spacedBy(Space.s8),
        ) {
            FilterChip("All", app.matchFilter == MatchFilter.All, { app.matchFilter = MatchFilter.All })
            FilterChip("Girls", app.matchFilter == MatchFilter.Girls, { app.matchFilter = MatchFilter.Girls })
            FilterChip("Boys", app.matchFilter == MatchFilter.Boys, { app.matchFilter = MatchFilter.Boys })
        }
        Text(
            "Near Lahore, Punjab · plus other countries · tap a person",
            style = AppType.caption,
            color = colors.textSecondary,
            modifier = Modifier.padding(top = Space.s12, bottom = Space.s8),
        )
        Box(Modifier.weight(1f).fillMaxWidth()) {
            Radar(
                people = people,
                soundOn = app.radarSound,
                onToggleSound = {
                    app.radarSound = !app.radarSound
                    if (app.radarSound) playBlip()
                },
                onPick = { selected = it },
            )
        }
        selected?.let { person ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = Space.s12)
                    .clip(RoundedCornerShape(Radius.card))
                    .background(colors.surfaceElevated)
                    .padding(Space.s12),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Avatar(person, size = 44.dp, showOnline = true)
                Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
                    Text(person.name, style = AppType.title, color = colors.textPrimary)
                    Text("${person.city}, ${person.country}", style = AppType.caption, color = colors.textSecondary)
                }
                IconCircleButton(Lucide.MessageCircle, "Chat", { onOpenChat(person.id) }, tint = colors.primary)
                IconCircleButton(
                    Lucide.Phone,
                    "Call",
                    { onCall(person.id, false) },
                    tint = colors.online,
                    modifier = Modifier.padding(start = Space.s8),
                )
            }
        }
    }
}

@Composable
private fun Radar(
    people: List<Person>,
    soundOn: Boolean,
    onToggleSound: () -> Unit,
    onPick: (Person) -> Unit,
) {
    val colors = LarkiTheme.colors
    val spin = rememberInfiniteTransition(label = "radar")
    val sweep by spin.animateFloat(
        0f,
        360f,
        infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep",
    )
    val pulse by spin.animateFloat(
        0.7f,
        1f,
        infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulse",
    )
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val side = maxWidth
        Canvas(Modifier.fillMaxSize()) {
            val center = this.center
            val maxRadius = size.minDimension / 2f * 0.86f
            Distance.entries.forEachIndexed { index, _ ->
                drawCircle(
                    color = colors.border,
                    radius = maxRadius * (index + 1) / 4f,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f),
                )
            }
            drawArc(
                color = colors.primary.copy(alpha = 0.18f),
                startAngle = sweep - 28f,
                sweepAngle = 28f,
                useCenter = true,
                topLeft = androidx.compose.ui.geometry.Offset(center.x - maxRadius, center.y - maxRadius),
                size = androidx.compose.ui.geometry.Size(maxRadius * 2, maxRadius * 2),
            )
            drawCircle(colors.primary.copy(alpha = 0.35f * pulse), radius = 10f * pulse, center = center)
            drawCircle(colors.primary, radius = 5f, center = center)
        }
        Distance.entries.forEachIndexed { index, distance ->
            val fraction = (index + 1) / 4f * 0.86f
            val radius = side * fraction / 2
            val labelX = (side / 2) + radius * cos(Math.toRadians(-90.0)).toFloat() - 28.dp
            val labelY = (side / 2) + radius * sin(Math.toRadians(-90.0)).toFloat() - 10.dp
            Text(
                distance.label,
                style = AppType.caption,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset { IntOffset(labelX.roundToPx(), labelY.roundToPx()) }
                    .clip(RoundedCornerShape(Radius.chip))
                    .background(colors.surface)
                    .padding(horizontal = Space.s8, vertical = Space.s4),
            )
        }
        people.forEach { person ->
            val ring = person.distance.ordinal + 1
            val radius = side * (ring / 4f) * 0.86f / 2
            val rad = Math.toRadians(person.angle.toDouble())
            val x = side / 2 + radius * cos(rad).toFloat() - 18.dp
            val y = side / 2 + radius * sin(rad).toFloat() - 18.dp
            Avatar(
                person = person,
                size = 36.dp,
                showOnline = person.online,
                letter = person.letter,
                modifier = Modifier
                    .offset { IntOffset(x.roundToPx(), y.roundToPx()) }
                    .clip(CircleShape)
                    .clickable { onPick(person) },
            )
        }
        IconCircleButton(
            icon = if (soundOn) Lucide.Volume2 else Lucide.VolumeX,
            contentDescription = if (soundOn) "Sound on" else "Sound off",
            onClick = onToggleSound,
            modifier = Modifier.align(Alignment.TopEnd),
        )
    }
}

private fun playBlip() {
    try {
        val tone = android.media.ToneGenerator(android.media.AudioManager.STREAM_NOTIFICATION, 30)
        tone.startTone(android.media.ToneGenerator.TONE_PROP_ACK, 70)
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({ tone.release() }, 180)
    } catch (_: RuntimeException) {
    }
}
