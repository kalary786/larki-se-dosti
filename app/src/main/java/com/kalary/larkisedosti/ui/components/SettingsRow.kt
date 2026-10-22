package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    chevron: Boolean = false,
    checked: Boolean? = null,
    titleColor: Color? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val colors = LarkiTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null || onCheckedChange != null) {
                if (onCheckedChange != null && checked != null) onCheckedChange(!checked) else onClick?.invoke()
            }
            .padding(horizontal = Space.s16, vertical = Space.s12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = titleColor ?: colors.textPrimary, modifier = Modifier.size(20.dp))
        Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
            Text(title, style = AppType.title, color = titleColor ?: colors.textPrimary)
            if (subtitle != null) {
                Text(subtitle, style = AppType.caption, color = colors.textSecondary)
            }
        }
        if (checked != null) {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = colors.primary,
                    checkedThumbColor = colors.onPrimary,
                    uncheckedTrackColor = colors.surfaceElevated,
                    uncheckedThumbColor = colors.textSecondary,
                    uncheckedBorderColor = colors.border,
                ),
            )
        } else if (chevron) {
            Icon(Lucide.ChevronRight, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(18.dp))
        }
    }
}
