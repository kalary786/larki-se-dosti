package com.larkisedosti.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.theme.LoungeMotion
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.theme.pressScale

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LoungeTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val background by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.surface,
        animationSpec = tween(LoungeMotion.Standard),
        label = "chip",
    )
    val content by animateColorAsState(
        targetValue = if (selected) colors.onPrimary else colors.textSecondary,
        animationSpec = tween(LoungeMotion.Standard),
        label = "chip-text",
    )
    val border by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.border,
        animationSpec = tween(LoungeMotion.Standard),
        label = "chip-border",
    )
    Text(
        text = label,
        color = content,
        style = LoungeType.caption,
        modifier = modifier
            .pressScale(interaction)
            .clip(LoungeShape.chip)
            .background(background)
            .border(1.dp, border, LoungeShape.chip)
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = content.copy(alpha = 0.2f)),
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = LoungeSpace.s8),
    )
}
