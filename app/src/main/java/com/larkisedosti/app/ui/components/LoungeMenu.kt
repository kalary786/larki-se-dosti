package com.larkisedosti.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

data class MenuEntry(
    val label: String,
    val icon: ImageVector,
    val danger: Boolean = false,
    val onClick: () -> Unit,
)

@Composable
fun LoungeMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    entries: List<MenuEntry>,
) {
    val colors = LoungeTheme.colors
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        containerColor = colors.surfaceElevated,
        shape = LoungeShape.card,
    ) {
        entries.forEach { entry ->
            val tint = if (entry.danger) colors.danger else colors.textPrimary
            Row(
                modifier = Modifier
                    .clickable {
                        onDismiss()
                        entry.onClick()
                    }
                    .padding(horizontal = LoungeSpace.s16, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = entry.icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(LoungeSpace.s12))
                Text(text = entry.label, color = tint, style = LoungeType.body)
            }
        }
    }
}

@Composable
fun MenuAnchor(
    expanded: Boolean,
    onDismiss: () -> Unit,
    entries: List<MenuEntry>,
    content: @Composable () -> Unit,
) {
    Column {
        content()
        LoungeMenu(expanded = expanded, onDismiss = onDismiss, entries = entries)
    }
}
