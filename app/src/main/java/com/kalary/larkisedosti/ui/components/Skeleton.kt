package com.kalary.larkisedosti.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Radius

@Composable
fun SkeletonCard(modifier: Modifier = Modifier) {
    val colors = LarkiTheme.colors
    val transition = rememberInfiniteTransition(label = "skeleton")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Restart),
        label = "shift",
    )
    val brush = Brush.linearGradient(
        colors = listOf(colors.surface, colors.surfaceElevated, colors.surface),
        start = Offset(shift * 600f, 0f),
        end = Offset(shift * 600f + 280f, 280f),
    )
    androidx.compose.foundation.layout.Box(
        modifier
            .aspectRatio(0.72f)
            .clip(RoundedCornerShape(Radius.card))
            .background(brush),
    )
}
