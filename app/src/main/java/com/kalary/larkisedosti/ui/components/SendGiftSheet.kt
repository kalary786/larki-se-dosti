package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.Gift
import com.kalary.larkisedosti.data.MockData
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun SendGiftSheet(
    name: String,
    balance: Int,
    onClose: () -> Unit,
    onBuyCoins: () -> Unit,
    onSend: (Gift) -> Unit,
) {
    val colors = LarkiTheme.colors
    AppBottomSheet(onClose) {
        Column(Modifier.padding(horizontal = Space.s16).padding(bottom = Space.s24)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BoxIcon()
                Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
                    Text("Send a Gift", style = AppType.title, color = colors.textPrimary)
                    Text("To $name", style = AppType.caption, color = colors.textSecondary)
                }
                CoinPill(balance, onBuyCoins)
                IconCircleButton(
                    Lucide.X,
                    "Close",
                    onClose,
                    modifier = Modifier.padding(start = Space.s8),
                    size = 36.dp,
                    iconSize = 16.dp,
                )
            }
            Text(
                "They see a live animation when it lands.",
                style = AppType.caption,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = Space.s16, bottom = Space.s12),
            )
            MockData.gifts.chunked(3).forEach { row ->
                Row(
                    Modifier.fillMaxWidth().padding(bottom = Space.s8),
                    horizontalArrangement = Arrangement.spacedBy(Space.s8),
                ) {
                    row.forEach { gift ->
                        GiftCell(gift, Modifier.weight(1f)) { onSend(gift) }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxIcon() {
    val colors = LarkiTheme.colors
    androidx.compose.foundation.layout.Box(
        Modifier.size(44.dp).clip(CircleShape).background(colors.surfaceElevated),
        contentAlignment = Alignment.Center,
    ) {
        Icon(Lucide.Gift, contentDescription = null, tint = colors.primary, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun GiftCell(gift: Gift, modifier: Modifier, onClick: () -> Unit) {
    val colors = LarkiTheme.colors
    val shape = RoundedCornerShape(Radius.card)
    Column(
        modifier
            .clip(shape)
            .background(colors.surfaceElevated)
            .border(1.dp, colors.border, shape)
            .clickable(onClick = onClick)
            .padding(Space.s8),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GiftArt(gift.kind, Modifier.size(48.dp))
        Text(gift.name, style = AppType.caption, color = colors.textPrimary, textAlign = TextAlign.Center)
        Row(
            Modifier
                .padding(top = Space.s4)
                .clip(RoundedCornerShape(Radius.chip))
                .background(colors.surface)
                .padding(horizontal = Space.s8, vertical = Space.s4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.s4),
        ) {
            Icon(Lucide.Coins, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(12.dp))
            Text(gift.cost.toString(), style = AppType.caption, color = colors.textPrimary)
        }
    }
}
