package com.kalary.larkisedosti.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.navigation.Routes
import com.kalary.larkisedosti.ui.components.LogoMark
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets
import com.kalary.larkisedosti.ui.theme.tag
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(onFinished: (String) -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val progress = remember { Animatable(0f) }
    val drift = rememberInfiniteTransition(label = "glow")
    val angle by drift.animateFloat(
        0f,
        360f,
        infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "angle",
    )
    LaunchedEffect(Unit) {
        progress.animateTo(1f, tween(2000, easing = LinearEasing))
        app.onSplashFinished()
        onFinished(if (app.onboarded) Routes.Main else Routes.Onboarding)
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .tag("screen_splash"),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val radians = Math.toRadians(angle.toDouble())
            val dx = cos(radians).toFloat() * size.minDimension * 0.08f
            val dy = sin(radians).toFloat() * size.minDimension * 0.06f
            drawCircle(
                color = colors.primary.copy(alpha = 0.22f),
                radius = size.minDimension * 0.42f,
                center = center + Offset(dx, dy),
            )
            drawCircle(
                color = colors.secondary.copy(alpha = 0.10f),
                radius = size.minDimension * 0.28f,
                center = center + Offset(-dx, -dy * 0.5f),
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .screenInsets()
                .padding(Space.s24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LogoMark()
            Spacer(Modifier.height(Space.s20))
            Text("Larki Se Dosti", style = AppType.h1, color = colors.textPrimary, textAlign = TextAlign.Center)
            Text(
                "Live friends, real voices",
                style = AppType.caption,
                color = colors.textPrimary,
                modifier = Modifier
                    .padding(top = Space.s12)
                    .clip(RoundedCornerShape(Radius.chip))
                    .background(colors.surfaceElevated)
                    .padding(horizontal = Space.s16, vertical = Space.s8),
            )
            Spacer(Modifier.height(Space.s32))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(Radius.chip))
                    .background(colors.border),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(progress.value.coerceIn(0.04f, 1f))
                        .height(3.dp)
                        .background(colors.primary),
                )
            }
            Text(
                "Tuning the room to your pace…",
                style = AppType.body,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = Space.s16),
            )
        }
    }
}
