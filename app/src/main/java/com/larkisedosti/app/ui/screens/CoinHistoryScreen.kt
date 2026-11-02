package com.larkisedosti.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.LedgerEntry
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import java.text.DateFormat
import java.util.Date

@Composable
fun CoinHistoryScreen(
    store: AppStore,
    onBack: () -> Unit,
) {
    val colors = LoungeTheme.colors
    var rows by remember { mutableStateOf<List<LedgerEntry>>(emptyList()) }
    DisposableEffect(store.userId) {
        val registration = store.listenLedger { rows = it }
        onDispose { registration?.remove() }
    }
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
                Text(text = "Coin history", color = colors.textPrimary, style = LoungeType.h2)
                Text(text = "Balance ${store.coins}", color = colors.coin, style = LoungeType.body)
            }
        }
        Spacer(Modifier.height(LoungeSpace.s20))
        if (rows.isEmpty()) {
            Text(
                text = "No coin history yet. A welcome bonus and each gift or call will show up here.",
                color = colors.textSecondary,
                style = LoungeType.body,
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(rows, key = { it.id }) { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = LoungeSpace.s8)
                            .clip(LoungeShape.card)
                            .background(colors.surface)
                            .border(1.dp, colors.border, LoungeShape.card)
                            .padding(LoungeSpace.s16),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ledgerLabel(entry.type),
                                color = colors.textPrimary,
                                style = LoungeType.title,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = ledgerTime(entry.createdAt),
                                color = colors.textSecondary,
                                style = LoungeType.caption,
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (entry.amount > 0) "+${entry.amount}" else entry.amount.toString(),
                                color = colors.coin,
                                style = LoungeType.title,
                            )
                            Text(
                                text = "Balance ${entry.balanceAfter}",
                                color = colors.textSecondary,
                                style = LoungeType.caption,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun ledgerLabel(type: String): String = when (type) {
    "purchase" -> "Purchase"
    "gift_sent" -> "Gift sent"
    "reward_ad" -> "Video reward"
    "call" -> "Call"
    "signup_bonus" -> "Welcome bonus"
    "message" -> "Message"
    "admin" -> "Adjustment"
    else -> "Coins"
}

private fun ledgerTime(millis: Long): String {
    if (millis <= 0L) return "Just now"
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(millis))
}
