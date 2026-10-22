package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

sealed interface SettingsTrailing {
    data object Chevron : SettingsTrailing
    data class Toggle(
        val checked: Boolean,
        val onCheckedChange: (Boolean) -> Unit,
    ) : SettingsTrailing
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null,
    trailing: SettingsTrailing = SettingsTrailing.Chevron,
    showDivider: Boolean = false,
) {
    val colors = LoungeTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val rowClick = if (trailing is SettingsTrailing.Toggle) null else onClick
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (rowClick != null) {
                        Modifier.clickable(
                            interactionSource = interaction,
                            indication = ripple(color = colors.textPrimary.copy(alpha = 0.08f)),
                            onClick = rowClick,
                        )
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = LoungeSpace.s16, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.textPrimary,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceElevated)
                    .padding(10.dp),
            )
            Spacer(Modifier.width(LoungeSpace.s12))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = colors.textPrimary, style = LoungeType.title)
                if (subtitle != null) {
                    Text(text = subtitle, color = colors.textSecondary, style = LoungeType.caption)
                }
            }
            when (trailing) {
                SettingsTrailing.Chevron -> {
                    Icon(
                        imageVector = AppIcons.ChevronRight,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(18.dp),
                    )
                }
                is SettingsTrailing.Toggle -> {
                    Switch(
                        checked = trailing.checked,
                        onCheckedChange = trailing.onCheckedChange,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colors.onPrimary,
                            checkedTrackColor = colors.primary,
                            uncheckedThumbColor = colors.textSecondary,
                            uncheckedTrackColor = colors.surfaceElevated,
                            uncheckedBorderColor = colors.outline,
                        ),
                    )
                }
            }
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 68.dp),
                thickness = 1.dp,
                color = colors.border,
            )
        }
    }
}
