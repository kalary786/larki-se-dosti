package com.larkisedosti.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.ModalDialog
import com.larkisedosti.app.ui.components.PrimaryButton
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

private data class CoinPack(val coins: Int, val price: String, val best: Boolean = false)

private val packs = listOf(
    CoinPack(50, "$0.99"),
    CoinPack(120, "$1.99"),
    CoinPack(300, "$3.99", best = true),
    CoinPack(700, "$7.99"),
    CoinPack(1500, "$14.99"),
)

@Composable
fun CoinStoreScreen(
    store: AppStore,
    onBack: () -> Unit,
) {
    val colors = LoungeTheme.colors
    var watch by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(LoungeSpace.s20),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircleButton(AppIcons.ChevronLeft, onBack, contentDescription = "Back", size = 40.dp)
            Spacer(Modifier.size(LoungeSpace.s12))
            Column {
                Text(text = "Coins", color = colors.textPrimary, style = LoungeType.h2)
                Text(text = "Balance ${store.coins}", color = colors.coin, style = LoungeType.body)
            }
        }
        Spacer(Modifier.height(LoungeSpace.s20))
        packs.forEach { pack ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LoungeSpace.s8)
                    .clip(LoungeShape.card)
                    .background(colors.surface)
                    .border(1.dp, if (pack.best) colors.primary else colors.border, LoungeShape.card)
                    .clickable { store.grantCoins(pack.coins) }
                    .padding(LoungeSpace.s16),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(AppIcons.Coin, contentDescription = null, tint = colors.coin, modifier = Modifier.size(22.dp))
                Spacer(Modifier.size(LoungeSpace.s12))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${pack.coins} coins",
                        color = colors.textPrimary,
                        style = LoungeType.title,
                    )
                    if (pack.best) {
                        Text(
                            text = "Best value",
                            color = colors.primary,
                            style = LoungeType.caption.copy(fontWeight = FontWeight.SemiBold),
                        )
                    }
                }
                Text(text = pack.price, color = colors.textPrimary, style = LoungeType.title)
            }
        }
        Spacer(Modifier.height(LoungeSpace.s8))
        PrimaryButton(
            text = "Watch a video to earn free coins",
            onClick = { watch = true },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    ModalDialog(
        visible = watch,
        title = "Rewarded video",
        body = "This is a placeholder for a rewarded ad. When it finishes, you get 10 coins.",
        confirmLabel = "Collect 10",
        dismissLabel = "Close",
        onConfirm = {
            store.grantCoins(10)
            watch = false
        },
        onDismiss = { watch = false },
    )
}
