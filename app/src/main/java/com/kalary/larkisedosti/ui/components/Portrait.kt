package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.kalary.larkisedosti.ui.theme.LarkiTheme

@Composable
fun Portrait(
    seed: Int,
    modifier: Modifier = Modifier,
    variant: Int = 0,
) {
    val colors = LarkiTheme.colors
    val background = colors.portraits[(seed + variant).mod(colors.portraits.size)]
    val skin = colors.skins[(seed + variant).mod(colors.skins.size)]
    val hair = colors.hair[(seed / 2 + variant).mod(colors.hair.size)]
    val shirt = colors.shirts[(seed + variant).mod(colors.shirts.size)]
    val ink = colors.ink
    Canvas(modifier) {
        val width = size.width
        val height = size.height
        drawRect(background)
        drawCircle(shirt, radius = width * 0.48f, center = Offset(width / 2f, height * 0.92f))
        drawCircle(hair, radius = width * 0.25f, center = Offset(width / 2f, height * 0.38f))
        drawCircle(skin, radius = width * 0.18f, center = Offset(width / 2f, height * 0.42f))
        drawArc(
            color = hair,
            startAngle = 200f,
            sweepAngle = 140f,
            useCenter = true,
            topLeft = Offset(width * 0.30f, height * 0.24f),
            size = Size(width * 0.40f, height * 0.26f),
        )
        drawCircle(ink, radius = width * 0.015f, center = Offset(width * 0.43f, height * 0.43f))
        drawCircle(ink, radius = width * 0.015f, center = Offset(width * 0.57f, height * 0.43f))
    }
}
