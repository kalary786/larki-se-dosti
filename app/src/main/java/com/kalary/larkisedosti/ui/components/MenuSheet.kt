package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Space

data class MenuEntry(
    val icon: ImageVector,
    val label: String,
    val danger: Boolean = false,
    val onClick: () -> Unit,
)

@Composable
fun MenuSheet(
    title: String,
    entries: List<MenuEntry>,
    onDismiss: () -> Unit,
) {
    val colors = LarkiTheme.colors
    AppBottomSheet(onDismiss) {
        Column(Modifier.padding(bottom = Space.s16)) {
            Text(
                title,
                style = AppType.h2,
                color = colors.textPrimary,
                modifier = Modifier.padding(horizontal = Space.s16, vertical = Space.s8),
            )
            entries.forEach { entry ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDismiss()
                            entry.onClick()
                        }
                        .padding(horizontal = Space.s16, vertical = Space.s12),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        entry.icon,
                        contentDescription = null,
                        tint = if (entry.danger) colors.danger else colors.textPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        entry.label,
                        style = AppType.title,
                        color = if (entry.danger) colors.danger else colors.textPrimary,
                        modifier = Modifier.padding(start = Space.s12),
                    )
                }
            }
        }
    }
}
