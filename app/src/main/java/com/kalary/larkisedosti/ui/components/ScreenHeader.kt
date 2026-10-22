package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun ScreenHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val colors = LarkiTheme.colors
    Row(
        modifier.fillMaxWidth().padding(horizontal = Space.s16, vertical = Space.s8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconCircleButton(Lucide.ArrowLeft, "Back", onBack)
        Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
            Text(title, style = AppType.title, color = colors.textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (subtitle != null) {
                Text(subtitle, style = AppType.caption, color = colors.textSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        actions()
    }
}
