package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.CoinGate
import com.kalary.larkisedosti.data.GateAction
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MockData
import com.kalary.larkisedosti.ui.components.Avatar
import com.kalary.larkisedosti.ui.components.EmptyState
import com.kalary.larkisedosti.ui.components.GhostButton
import com.kalary.larkisedosti.ui.components.IconCircleButton
import com.kalary.larkisedosti.ui.components.MenuEntry
import com.kalary.larkisedosti.ui.components.MenuSheet
import com.kalary.larkisedosti.ui.components.Portrait
import com.kalary.larkisedosti.ui.components.PrimaryButton
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets
import com.kalary.larkisedosti.ui.theme.tag
import kotlinx.coroutines.delay

@Composable
fun CallScreen(
    personId: String,
    video: Boolean,
    incoming: Boolean,
    onEnd: () -> Unit,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val person = app.person(personId)
    var connected by remember { mutableStateOf(false) }
    var seconds by remember { mutableIntStateOf(0) }
    var muted by remember { mutableStateOf(false) }
    var speaker by remember { mutableStateOf(true) }
    var cameraOn by remember { mutableStateOf(video) }
    var menu by remember { mutableStateOf(false) }
    LaunchedEffect(incoming) {
        if (!incoming) {
            delay(1200)
            connected = true
        }
    }
    LaunchedEffect(app.freePass) {
        if (app.freePass && incoming && !connected) {
            app.freePass = false
            connected = true
        }
    }
    LaunchedEffect(connected) {
        if (!connected) return@LaunchedEffect
        while (true) {
            delay(1000)
            seconds += 1
        }
    }
    if (person == null) {
        EmptyState(Lucide.Phone, "Call ended", "This person is no longer available.")
        return
    }
    val status = when {
        incoming && !connected -> if (video) "Incoming video call" else "Incoming voice call"
        !connected -> "Calling…"
        else -> "%02d:%02d".format(seconds / 60, seconds % 60)
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets()
            .tag("screen_call")
            .padding(Space.s16),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconCircleButton(Lucide.Flag, "Report", { app.reportFor = person.id })
            IconCircleButton(Lucide.Ban, "Block", { app.blockFor = person.id }, modifier = Modifier.padding(start = Space.s8), tint = colors.danger)
        }
        Text(status, style = AppType.h2, color = if (connected) colors.textPrimary else colors.textSecondary, modifier = Modifier.padding(top = Space.s24))
        Text("This call costs ${MockData.CALL_COST} coins", style = AppType.caption, color = colors.textSecondary)
        Box(
            Modifier
                .padding(vertical = Space.s32)
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(Radius.card)),
            contentAlignment = Alignment.Center,
        ) {
            if (cameraOn && (video || connected)) {
                Portrait(person.portrait, modifier = Modifier.fillMaxSize())
            } else {
                Avatar(person, size = 140.dp, showOnline = person.online, ring = true)
            }
        }
        Text(person.name, style = AppType.h2, color = colors.textPrimary, textAlign = TextAlign.Center)
        Text("${person.flag} ${person.country}", style = AppType.body, color = colors.textSecondary)
        if (incoming && !connected) {
            Row(Modifier.fillMaxWidth().padding(top = Space.s24), horizontalArrangement = Arrangement.spacedBy(Space.s12)) {
                GhostButton("Decline", onEnd, Modifier.weight(1f))
                PrimaryButton(
                    "Accept",
                    {
                        if (app.spend(MockData.CALL_COST)) {
                            connected = true
                        } else {
                            app.gate = CoinGate(
                                need = MockData.CALL_COST,
                                pill = "This call costs 10 coins",
                                action = GateAction.Call(person.id, video, incoming = true),
                            )
                        }
                    },
                    Modifier.weight(1f),
                )
            }
        } else {
            Row(
                Modifier.fillMaxWidth().padding(top = Space.s24),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconCircleButton(if (muted) Lucide.MicOff else Lucide.Mic, "Mute", { muted = !muted }, tint = if (muted) colors.primary else colors.textPrimary)
                IconCircleButton(if (speaker) Lucide.Volume2 else Lucide.VolumeX, "Speaker", { speaker = !speaker })
                IconCircleButton(if (cameraOn) Lucide.Camera else Lucide.CameraOff, "Camera", { cameraOn = !cameraOn })
                IconCircleButton(Lucide.Phone, "End call", onEnd, tint = colors.onPrimary, background = colors.danger)
            }
        }
        Text(
            "More",
            style = AppType.caption,
            color = colors.textSecondary,
            modifier = Modifier.padding(top = Space.s16).clickableMore { menu = true },
        )
    }
    if (menu) {
        MenuSheet(
            title = "Call options",
            entries = listOf(
                MenuEntry(Lucide.Flag, "Report") { app.reportFor = person.id },
                MenuEntry(Lucide.Ban, "Block", danger = true) { app.blockFor = person.id },
            ),
            onDismiss = { menu = false },
        )
    }
}

private fun Modifier.clickableMore(onClick: () -> Unit): Modifier = clickable(onClick = onClick)
