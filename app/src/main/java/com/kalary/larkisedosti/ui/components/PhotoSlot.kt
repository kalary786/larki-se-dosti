package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun PhotoSlot(
    filled: Boolean,
    seed: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LarkiTheme.colors
    val shape = RoundedCornerShape(Radius.input)
    Box(
        modifier
            .aspectRatio(1f)
            .clip(shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (filled) {
            Portrait(seed = seed, modifier = Modifier.fillMaxSize())
            Text(
                "Pending review",
                style = AppType.caption,
                color = colors.onPhoto,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(Space.s8)
                    .clip(RoundedCornerShape(Radius.chip))
                    .background(colors.overlay)
                    .padding(horizontal = Space.s8, vertical = Space.s4),
            )
        } else {
            Canvas(Modifier.fillMaxSize()) {
                drawRoundRect(
                    color = colors.border,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f)),
                    ),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                )
            }
            Icon(Lucide.CameraPlus, contentDescription = "Add photo", tint = colors.textSecondary, modifier = Modifier.size(28.dp))
        }
    }
}
