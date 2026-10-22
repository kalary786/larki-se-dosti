package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.theme.pressScale

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    val colors = LoungeTheme.colors
    LoungeButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        container = colors.primary,
        content = colors.onPrimary,
        border = null,
    )
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    val colors = LoungeTheme.colors
    LoungeButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        container = Color.Transparent,
        content = colors.textPrimary,
        border = colors.outline,
    )
}

@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    LoungeButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        container = Color.Transparent,
        content = LoungeTheme.colors.primary,
        border = null,
    )
}

@Composable
internal fun LoungeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    icon: ImageVector?,
    container: Color,
    content: Color,
    border: Color?,
) {
    val shape = LoungeShape.button
    val interaction = remember { MutableInteractionSource() }
    val colors = LoungeTheme.colors
    val fill = if (enabled) container else colors.surface
    val labelColor = if (enabled) content else colors.textSecondary
    val edge = if (enabled) border else colors.border
    Row(
        modifier = modifier
            .pressScale(interaction, enabled)
            .heightIn(min = 48.dp)
            .clip(shape)
            .background(fill)
            .then(
                if (edge != null) Modifier.border(1.dp, edge, shape) else Modifier
            )
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = labelColor.copy(alpha = 0.18f)),
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = LoungeSpace.s20, vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = labelColor,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(LoungeSpace.s8))
        }
        Text(
            text = text,
            color = labelColor,
            style = LoungeType.body.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}
