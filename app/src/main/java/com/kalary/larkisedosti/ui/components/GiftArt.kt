package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import com.kalary.larkisedosti.data.GiftKind
import com.kalary.larkisedosti.ui.theme.LarkiTheme

@Composable
fun GiftArt(kind: GiftKind, modifier: Modifier = Modifier) {
    val colors = LarkiTheme.colors
    Canvas(modifier) {
        val w = size.minDimension
        val c = Offset(size.width / 2f, size.height / 2f)
        when (kind) {
            GiftKind.Rose -> {
                drawLine(colors.online, Offset(c.x, c.y), Offset(c.x, c.y + w * 0.34f), strokeWidth = w * 0.06f)
                drawCircle(colors.danger, w * 0.16f, Offset(c.x, c.y - w * 0.08f))
                drawCircle(colors.danger.copy(alpha = 0.75f), w * 0.10f, Offset(c.x - w * 0.12f, c.y - w * 0.02f))
                drawCircle(colors.primary, w * 0.10f, Offset(c.x + w * 0.12f, c.y - w * 0.02f))
            }
            GiftKind.Heart -> {
                val path = Path().apply {
                    moveTo(c.x, c.y + w * 0.28f)
                    cubicTo(c.x - w * 0.45f, c.y - w * 0.05f, c.x - w * 0.28f, c.y - w * 0.38f, c.x, c.y - w * 0.08f)
                    cubicTo(c.x + w * 0.28f, c.y - w * 0.38f, c.x + w * 0.45f, c.y - w * 0.05f, c.x, c.y + w * 0.28f)
                    close()
                }
                drawPath(path, colors.danger)
            }
            GiftKind.Star -> {
                val path = Path()
                val points = 5
                val outer = w * 0.34f
                val inner = w * 0.15f
                for (i in 0 until points * 2) {
                    val radius = if (i % 2 == 0) outer else inner
                    val angle = Math.toRadians(-90.0 + i * 36.0)
                    val x = c.x + (kotlin.math.cos(angle) * radius).toFloat()
                    val y = c.y + (kotlin.math.sin(angle) * radius).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(path, colors.secondary)
            }
            GiftKind.Bouquet -> {
                drawCircle(colors.danger, w * 0.12f, Offset(c.x, c.y - w * 0.08f))
                drawCircle(colors.primary, w * 0.12f, Offset(c.x - w * 0.14f, c.y))
                drawCircle(colors.secondary, w * 0.12f, Offset(c.x + w * 0.14f, c.y))
                drawRoundRect(
                    colors.online,
                    topLeft = Offset(c.x - w * 0.08f, c.y + w * 0.08f),
                    size = Size(w * 0.16f, w * 0.22f),
                    cornerRadius = CornerRadius(w * 0.06f, w * 0.06f),
                )
            }
            GiftKind.Diamond -> {
                val path = Path().apply {
                    moveTo(c.x, c.y - w * 0.32f)
                    lineTo(c.x + w * 0.26f, c.y)
                    lineTo(c.x, c.y + w * 0.32f)
                    lineTo(c.x - w * 0.26f, c.y)
                    close()
                }
                drawPath(path, colors.primary)
                drawCircle(colors.onPrimary.copy(alpha = 0.35f), w * 0.06f, Offset(c.x - w * 0.04f, c.y - w * 0.06f))
            }
            GiftKind.Crown -> {
                val path = Path().apply {
                    moveTo(c.x - w * 0.32f, c.y + w * 0.16f)
                    lineTo(c.x - w * 0.28f, c.y - w * 0.08f)
                    lineTo(c.x - w * 0.10f, c.y + w * 0.04f)
                    lineTo(c.x, c.y - w * 0.28f)
                    lineTo(c.x + w * 0.10f, c.y + w * 0.04f)
                    lineTo(c.x + w * 0.28f, c.y - w * 0.08f)
                    lineTo(c.x + w * 0.32f, c.y + w * 0.16f)
                    close()
                }
                drawPath(path, colors.secondary)
                drawRoundRect(
                    colors.primary,
                    topLeft = Offset(c.x - w * 0.32f, c.y + w * 0.16f),
                    size = Size(w * 0.64f, w * 0.10f),
                    cornerRadius = CornerRadius(w * 0.04f, w * 0.04f),
                )
            }
        }
    }
}
