package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    val colors = LarkiTheme.colors
    val shape = RoundedCornerShape(Radius.input)
    Row(
        modifier
            .fillMaxWidth()
            .background(colors.surface, shape)
            .border(1.dp, colors.border, shape)
            .padding(horizontal = Space.s12, vertical = Space.s12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Lucide.Search, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(18.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = AppType.body.copy(color = colors.textPrimary),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(start = Space.s8),
            decorationBox = { inner ->
                if (value.isEmpty()) {
                    Text(placeholder, style = AppType.body, color = colors.textSecondary)
                }
                inner()
            },
        )
    }
}
