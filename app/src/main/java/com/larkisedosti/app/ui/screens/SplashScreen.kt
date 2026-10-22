package com.larkisedosti.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun SplashScreen(
    message: String? = null,
    onRetry: () -> Unit = {},
) {
    val colors = LoungeTheme.colors
    val drift = rememberInfiniteTransition(label = "glow")
    val shift = drift.animateFloat(
        initialValue = -28f,
        targetValue = 36f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Reverse),
        label = "shift",
    )
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(1500))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val center = this.center + Offset(shift.value, -shift.value * 0.6f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(colors.primary.copy(alpha = 0.22f), Color.Transparent),
                    center = center,
                    radius = size.minDimension * 0.48f,
                ),
                radius = size.minDimension * 0.48f,
                center = center,
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(colors.coin.copy(alpha = 0.08f), Color.Transparent),
                    center = this.center + Offset(-40f, 80f),
                    radius = size.minDimension * 0.36f,
                ),
                radius = size.minDimension * 0.36f,
                center = this.center + Offset(-40f, 80f),
            )
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(colors.surface),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcons.Heart,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(36.dp),
                )
            }
            Spacer(Modifier.height(LoungeSpace.s20))
            Text(text = "Larki Se Dosti", color = colors.textPrimary, style = LoungeType.h1)
            Spacer(Modifier.height(LoungeSpace.s12))
            Text(
                text = "A quieter hello",
                color = colors.textSecondary,
                style = LoungeType.caption,
                modifier = Modifier
                    .clip(LoungeShape.chip)
                    .background(colors.surface)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
            Spacer(Modifier.height(LoungeSpace.s32))
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(3.dp)
                    .clip(LoungeShape.chip)
                    .background(colors.surface),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.value.coerceIn(0f, 1f))
                        .background(colors.primary),
                )
            }
            Spacer(Modifier.height(LoungeSpace.s12))
            Text(
                text = message ?: "Lowering the lights...",
                color = if (message == null) colors.textSecondary else colors.danger,
                style = LoungeType.caption,
                modifier = if (message == null) {
                    Modifier
                } else {
                    Modifier.clickable(onClick = onRetry)
                },
            )
        }
    }
}
