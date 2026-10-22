package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun AdSlot(
    modifier: Modifier = Modifier,
    label: String = "Ad",
    height: Dp = 60.dp,
    matchParent: Boolean = false,
) {
    val colors = LoungeTheme.colors
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (matchParent) Modifier.fillMaxSize() else Modifier.height(height))
            .clip(if (matchParent) LoungeShape.card else LoungeShape.button)
            .background(colors.surface)
            .then(
                if (matchParent) {
                    Modifier.border(1.dp, colors.border, LoungeShape.card)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = AppIcons.Image,
                contentDescription = null,
                tint = colors.textSecondary,
                modifier = Modifier.size(if (matchParent) 18.dp else 14.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = label,
                color = colors.textSecondary,
                style = LoungeType.caption,
            )
        }
    }
}
