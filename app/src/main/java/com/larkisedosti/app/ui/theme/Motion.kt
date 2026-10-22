package com.larkisedosti.app.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

object LoungeMotion {
    const val Fast = 200
    const val Standard = 250
    const val Pressed = 0.96f
}

@Composable
fun Modifier.pressScale(
    interaction: InteractionSource,
    enabled: Boolean = true,
): Modifier {
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) LoungeMotion.Pressed else 1f,
        animationSpec = tween(LoungeMotion.Fast),
        label = "press",
    )
    return graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

@Composable
fun Modifier.appear(): Modifier {
    val scale = remember { Animatable(0.96f) }
    val fade = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, tween(LoungeMotion.Standard)) }
        launch { fade.animateTo(1f, tween(LoungeMotion.Standard)) }
    }
    return graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
        alpha = fade.value
    }
}
