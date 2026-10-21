package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MockData
import com.kalary.larkisedosti.data.VideoReward
import com.kalary.larkisedosti.ui.components.ScreenHeader
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets
import com.kalary.larkisedosti.ui.theme.tag

@Composable
fun CoinStoreScreen(onBack: () -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets()
            .verticalScroll(rememberScrollState())
            .tag("screen_coins"),
    ) {
        ScreenHeader("Coin Store", onBack)
        Column(
            Modifier
                .padding(horizontal = Space.s16)
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.card))
                .background(colors.surfaceElevated)
                .padding(Space.s20),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(Lucide.Coins, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(36.dp))
            Text("Balance", style = AppType.caption, color = colors.textSecondary, modifier = Modifier.padding(top = Space.s8))
            Text(app.coins.toString(), style = AppType.h1, color = colors.textPrimary)
        }
        Column(Modifier.padding(Space.s16), verticalArrangement = Arrangement.spacedBy(Space.s8)) {
            MockData.coinPacks.forEach { pack ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Radius.card))
                        .background(colors.surface)
                        .border(1.dp, if (pack.best) colors.primary else colors.border, RoundedCornerShape(Radius.card))
                        .clickable { app.buy(pack) }
                        .padding(Space.s16),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Lucide.Coins, contentDescription = null, tint = colors.secondary)
                    Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
                        Text("${pack.coins} coins", style = AppType.title, color = colors.textPrimary)
                        if (pack.best) {
                            Text(
                                "Best value",
                                style = AppType.caption,
                                color = colors.onPrimary,
                                modifier = Modifier
                                    .padding(top = Space.s4)
                                    .clip(RoundedCornerShape(Radius.chip))
                                    .background(colors.primary)
                                    .padding(horizontal = Space.s8, vertical = Space.s4),
                            )
                        }
                    }
                    Text(pack.price, style = AppType.title, color = colors.primary)
                }
            }
            Text(
                "Watch a video to earn free coins",
                style = AppType.title,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Space.s8)
                    .clip(RoundedCornerShape(Radius.button))
                    .border(1.dp, colors.primary, RoundedCornerShape(Radius.button))
                    .clickable { app.videoReward = VideoReward.Coins }
                    .padding(Space.s16),
            )
        }
    }
}
