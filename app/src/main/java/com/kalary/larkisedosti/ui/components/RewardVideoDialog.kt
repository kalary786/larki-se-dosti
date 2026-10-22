package com.kalary.larkisedosti.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun RewardVideoDialog(onFinished: () -> Unit) {
    val colors = LarkiTheme.colors
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(1400, easing = LinearEasing))
        onFinished()
    }
    ModalDialog(onDismiss = {}) {
        Text("Short video", style = AppType.h2, color = colors.textPrimary)
        Text(
            "A rewarded video would play here.",
            style = AppType.body,
            color = colors.textSecondary,
            modifier = Modifier.padding(top = Space.s8, bottom = Space.s16),
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(Radius.chip))
                .background(colors.border),
        ) {
            Box(
                Modifier
                    .fillMaxWidth(progress.value.coerceIn(0.02f, 1f))
                    .height(6.dp)
                    .background(colors.primary),
            )
        }
        Text(
            "Playing…",
            style = AppType.caption,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = Space.s8),
        )
    }
}
