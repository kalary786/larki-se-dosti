package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun StatTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    val colors = LarkiTheme.colors
    val shape = RoundedCornerShape(Radius.card)
    Column(
        modifier = modifier
            .shadow(6.dp, shape, ambientColor = colors.shadow, spotColor = colors.shadow)
            .background(colors.surfaceElevated, shape)
            .border(1.dp, colors.border, shape)
            .padding(vertical = Space.s12, horizontal = Space.s8),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, style = AppType.h2, color = colors.textPrimary)
        Text(label, style = AppType.caption, color = colors.textSecondary)
    }
}
