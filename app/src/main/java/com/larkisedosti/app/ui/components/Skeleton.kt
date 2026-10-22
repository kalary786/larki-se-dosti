package com.larkisedosti.app.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme

@Composable
fun rememberSkeletonPulse(): Float {
    val pulse = rememberInfiniteTransition(label = "skeleton")
    val alpha by pulse.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "skeleton-alpha",
    )
    return alpha
}

@Composable
fun DiscoverSkeletonCard(
    pulse: Float,
    modifier: Modifier = Modifier,
) {
    val colors = LoungeTheme.colors
    Box(
        modifier = modifier
            .aspectRatio(0.68f)
            .clip(LoungeShape.card)
            .background(colors.surface.copy(alpha = pulse)),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(LoungeSpace.s12),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.62f)
                    .height(14.dp)
                    .clip(LoungeShape.chip)
                    .background(colors.surfaceElevated.copy(alpha = pulse)),
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(10.dp)
                    .clip(LoungeShape.chip)
                    .background(colors.surfaceElevated.copy(alpha = pulse)),
            )
        }
    }
}

@Composable
fun ChatSkeletonRow(pulse: Float) {
    val colors = LoungeTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LoungeSpace.s12, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(colors.surfaceElevated.copy(alpha = pulse)),
        )
        Spacer(Modifier.width(LoungeSpace.s12))
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.42f)
                    .height(12.dp)
                    .clip(LoungeShape.chip)
                    .background(colors.surfaceElevated.copy(alpha = pulse)),
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(10.dp)
                    .clip(LoungeShape.chip)
                    .background(colors.surfaceElevated.copy(alpha = pulse)),
            )
        }
    }
}
