package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.LarkiTheme

@Composable
fun LogoMark(modifier: Modifier = Modifier, markSize: Dp = 96.dp) {
    val colors = LarkiTheme.colors
    Canvas(modifier.size(markSize)) {
        val width = size.width
        rotate(-16f, pivot = Offset(width * 0.38f, width * 0.55f)) {
            drawRoundRect(
                color = colors.primary,
                topLeft = Offset(width * 0.20f, width * 0.16f),
                size = Size(width * 0.26f, width * 0.62f),
                cornerRadius = CornerRadius(width * 0.13f, width * 0.13f),
            )
        }
        rotate(16f, pivot = Offset(width * 0.62f, width * 0.55f)) {
            drawRoundRect(
                color = colors.primary.copy(alpha = 0.72f),
                topLeft = Offset(width * 0.54f, width * 0.16f),
                size = Size(width * 0.26f, width * 0.62f),
                cornerRadius = CornerRadius(width * 0.13f, width * 0.13f),
            )
        }
        drawCircle(colors.secondary, radius = width * 0.07f, center = Offset(width * 0.50f, width * 0.40f))
    }
}
