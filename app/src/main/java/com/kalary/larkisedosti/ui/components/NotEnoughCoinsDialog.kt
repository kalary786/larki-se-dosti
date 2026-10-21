package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun NotEnoughCoinsDialog(
    balance: Int,
    need: Int,
    pill: String,
    onBuy: () -> Unit,
    onWatch: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = LarkiTheme.colors
    ModalDialog(onDismiss = onDismiss) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(
                Modifier.size(64.dp).clip(CircleShape).background(colors.surfaceElevated),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Lucide.Coins, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(28.dp))
            }
        }
        Text(
            "Not enough coins",
            style = AppType.h2,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = Space.s16),
        )
        Text(
            "Buy coins, or watch a video for one free try.",
            style = AppType.body,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Space.s8),
        )
        Row(
            Modifier.fillMaxWidth().padding(top = Space.s16),
            horizontalArrangement = Arrangement.spacedBy(Space.s8),
        ) {
            CoinStat("You have", balance, Modifier.weight(1f))
            CoinStat("Need", need, Modifier.weight(1f))
        }
        Text(
            pill,
            style = AppType.caption,
            color = colors.textPrimary,
            modifier = Modifier
                .padding(top = Space.s12)
                .clip(RoundedCornerShape(Radius.chip))
                .background(colors.surfaceElevated)
                .border(1.dp, colors.border, RoundedCornerShape(Radius.chip))
                .padding(horizontal = Space.s12, vertical = Space.s8)
                .align(Alignment.CenterHorizontally),
        )
        PrimaryButton("Buy Coins", onBuy, Modifier.fillMaxWidth().padding(top = Space.s16))
        SecondaryButton("Watch video — free call", onWatch, Modifier.fillMaxWidth().padding(top = Space.s8))
        GhostButton("Not now", onDismiss, Modifier.fillMaxWidth())
    }
}

@Composable
private fun CoinStat(label: String, value: Int, modifier: Modifier) {
    val colors = LarkiTheme.colors
    Column(
        modifier
            .clip(RoundedCornerShape(Radius.card))
            .background(colors.surfaceElevated)
            .border(1.dp, colors.border, RoundedCornerShape(Radius.card))
            .padding(Space.s12),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Space.s4),
    ) {
        Icon(Lucide.Coins, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(16.dp))
        Text(label, style = AppType.caption, color = colors.textSecondary)
        Text(value.toString(), style = AppType.title, color = colors.textPrimary)
    }
}
