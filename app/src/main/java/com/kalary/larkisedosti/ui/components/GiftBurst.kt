package com.kalary.larkisedosti.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.Gift
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Motion

@Composable
fun GiftBurstOverlay(gift: Gift, onDone: () -> Unit) {
    val colors = LarkiTheme.colors
    val progress = remember { Animatable(0f) }
    LaunchedEffect(gift.id) {
        progress.animateTo(1f, tween(Motion.gift))
        progress.animateTo(2f, tween(Motion.gift))
        onDone()
    }
    val value = progress.value
    val scale = if (value < 1f) 0.6f + 0.5f * value else 1.1f + (value - 1f) * 0.45f
    val alpha = if (value < 1f) value else (2f - value).coerceIn(0f, 1f)
    val interaction = remember { MutableInteractionSource() }
    Box(
        Modifier
            .fillMaxSize()
            .background(colors.scrim.copy(alpha = 0.45f * alpha))
            .clickable(interactionSource = interaction, indication = null, onClick = {}),
        contentAlignment = Alignment.Center,
    ) {
        GiftArt(
            gift.kind,
            Modifier
                .size(180.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                },
        )
    }
}
