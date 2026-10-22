package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun AdSlot(
    modifier: Modifier = Modifier,
    native: Boolean = false,
) {
    val colors = LarkiTheme.colors
    val shape = RoundedCornerShape(if (native) Radius.card else Radius.button)
    Box(
        modifier = modifier
            .then(if (native) Modifier.fillMaxSize() else Modifier.fillMaxWidth().height(60.dp))
            .clip(shape)
            .background(colors.surfaceElevated)
            .border(1.dp, colors.border, shape)
            .padding(Space.s12),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Space.s4),
        ) {
            Text(
                "Ad",
                style = AppType.caption,
                color = colors.onPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(Radius.chip))
                    .background(colors.primary)
                    .padding(horizontal = Space.s8, vertical = Space.s4),
            )
            Text(
                if (native) "A quiet spot for a sponsor" else "Sponsored",
                style = AppType.caption,
                color = colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}
