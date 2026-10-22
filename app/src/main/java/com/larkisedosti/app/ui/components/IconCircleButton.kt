package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.pressScale

@Composable
fun IconCircleButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 44.dp,
    tint: Color = Color.Unspecified,
    containerColor: Color = Color.Unspecified,
    rotation: Float = 0f,
) {
    val colors = LoungeTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val iconTint = if (tint == Color.Unspecified) colors.textPrimary else tint
    val fill = if (containerColor == Color.Unspecified) colors.surface else containerColor
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = iconTint,
        modifier = modifier
            .pressScale(interaction)
            .graphicsLayer { rotationZ = rotation }
            .size(size)
            .clip(CircleShape)
            .background(fill)
            .border(1.dp, colors.border, CircleShape)
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = iconTint.copy(alpha = 0.16f)),
                onClick = onClick,
            )
            .padding(size * 0.25f),
    )
}
