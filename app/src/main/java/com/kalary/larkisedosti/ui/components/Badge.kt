package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme

@Composable
fun CountBadge(
    count: Int,
    modifier: Modifier = Modifier,
) {
    if (count <= 0) return
    val colors = LarkiTheme.colors
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(colors.danger)
            .padding(horizontal = 5.dp, vertical = 1.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (count > 9) "9+" else count.toString(),
            style = AppType.caption,
            color = colors.onPrimary,
        )
    }
}
