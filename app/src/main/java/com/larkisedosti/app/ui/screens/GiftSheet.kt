package com.larkisedosti.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.larkisedosti.app.model.GiftKind
import com.larkisedosti.app.ui.components.BottomSheet
import com.larkisedosti.app.ui.components.CoinPill
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun SendGiftSheet(
    visible: Boolean,
    recipient: String,
    balance: Int,
    onClose: () -> Unit,
    onAddCoins: () -> Unit,
    onSend: (GiftKind) -> Unit,
) {
    val colors = LoungeTheme.colors
    var confirm by remember { mutableStateOf<GiftKind?>(null) }
    LaunchedEffect(visible) {
        if (!visible) confirm = null
    }
    BottomSheet(visible = visible, onDismiss = onClose) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(colors.surface),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcons.Gift,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(Modifier.size(LoungeSpace.s12))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Send a Gift", color = colors.textPrimary, style = LoungeType.title)
                Text(text = "To $recipient", color = colors.textSecondary, style = LoungeType.caption)
            }
            IconCircleButton(
                icon = AppIcons.Close,
                contentDescription = "Close",
                onClick = onClose,
                size = 36.dp,
            )
        }
        Spacer(Modifier.height(LoungeSpace.s12))
        CoinPill(balance = balance, onAddClick = onAddCoins)
        Spacer(Modifier.height(LoungeSpace.s12))
        Text(
            text = "They see a short animation when it arrives.",
            color = colors.textSecondary,
            style = LoungeType.body,
        )
        Spacer(Modifier.height(LoungeSpace.s16))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
            verticalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
            modifier = Modifier.height(320.dp),
        ) {
            items(GiftKind.entries, key = { it.name }) { gift ->
                val pending = confirm == gift
                Column(
                    modifier = Modifier
                        .clip(LoungeShape.card)
                        .background(colors.surface)
                        .border(1.dp, if (pending) colors.primary else colors.border, LoungeShape.card)
                        .clickable(enabled = !pending) { confirm = gift }
                        .padding(vertical = LoungeSpace.s12, horizontal = LoungeSpace.s8),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = gift.emoji(),
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(LoungeSpace.s8))
                    if (pending) {
                        Text(
                            text = "Send for ${gift.price} coins?",
                            color = colors.textPrimary,
                            style = LoungeType.caption,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(LoungeSpace.s8))
                        Text(
                            text = "Send",
                            color = colors.onPrimary,
                            style = LoungeType.caption,
                            modifier = Modifier
                                .clip(LoungeShape.button)
                                .background(colors.primary)
                                .clickable { onSend(gift) }
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                        )
                    } else {
                        Text(
                            text = gift.title,
                            color = colors.textPrimary,
                            style = LoungeType.caption,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .clip(LoungeShape.chip)
                                .background(colors.surfaceElevated)
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = AppIcons.Coin,
                                contentDescription = null,
                                tint = colors.coin,
                                modifier = Modifier.size(12.dp),
                            )
                            Spacer(Modifier.size(4.dp))
                            Text(text = gift.price.toString(), color = colors.textPrimary, style = LoungeType.caption)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GiftBurst(
    title: String,
    onFinished: () -> Unit,
) {
    val colors = LoungeTheme.colors
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(title) {
        alpha.animateTo(1f, tween(250))
        scale.animateTo(1.12f, tween(280))
        scale.animateTo(1f, tween(200))
        alpha.animateTo(0f, tween(250))
        onFinished()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { this.alpha = alpha.value }
            .background(colors.background.copy(alpha = 0.72f)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    this.alpha = alpha.value
                }
                .clip(CircleShape)
                .background(colors.primary.copy(alpha = 0.16f)),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
            },
        ) {
            Icon(
                imageVector = AppIcons.Gift,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(72.dp),
            )
            Spacer(Modifier.height(LoungeSpace.s12))
            Text(text = title, color = colors.textPrimary, style = LoungeType.h2)
        }
    }
}

private fun GiftKind.emoji(): String = when (this) {
    GiftKind.Rose -> "\uD83C\uDF39"
    GiftKind.Heart -> "\u2764\uFE0F"
    GiftKind.Star -> "\u2B50"
    GiftKind.Bouquet -> "\uD83D\uDC90"
    GiftKind.Diamond -> "\uD83D\uDC8E"
    GiftKind.Crown -> "\uD83D\uDC51"
}
