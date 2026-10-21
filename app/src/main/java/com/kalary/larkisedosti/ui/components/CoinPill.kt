package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.pressScale
import com.kalary.larkisedosti.ui.theme.tag

@Composable
fun CoinPill(
    balance: Int,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LarkiTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(Radius.chip)
    Row(
        modifier = modifier
            .tag("coin_pill")
            .pressScale(interaction)
            .clip(shape)
            .background(colors.surfaceElevated)
            .border(1.dp, colors.border, shape)
            .clickable(interactionSource = interaction, indication = null, onClick = onPlus)
            .padding(horizontal = Space.s12, vertical = Space.s8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Space.s8),
    ) {
        Icon(Lucide.Coins, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(16.dp))
        Text(balance.toString(), style = AppType.caption, color = colors.textPrimary)
        Icon(Lucide.Plus, contentDescription = "Buy coins", tint = colors.primary, modifier = Modifier.size(14.dp))
    }
}
