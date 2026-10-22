package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import java.util.Locale

@Composable
fun CoinPill(
    balance: Int,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LoungeTheme.colors
    val addInteraction = remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .clip(LoungeShape.chip)
            .background(colors.surface)
            .border(1.dp, colors.border, LoungeShape.chip)
            .padding(start = 10.dp, top = LoungeSpace.s4, end = LoungeSpace.s4, bottom = LoungeSpace.s4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = AppIcons.Coin,
            contentDescription = null,
            tint = colors.coin,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "%,d".format(Locale.US, balance),
            color = colors.textPrimary,
            style = LoungeType.body.copy(fontWeight = FontWeight.SemiBold),
        )
        Spacer(Modifier.width(LoungeSpace.s8))
        Icon(
            imageVector = AppIcons.Plus,
            contentDescription = "Add coins",
            tint = colors.textPrimary,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(colors.surfaceElevated)
                .border(1.dp, colors.outline, CircleShape)
                .clickable(
                    interactionSource = addInteraction,
                    indication = ripple(bounded = true, color = colors.textPrimary.copy(alpha = 0.2f)),
                    onClick = onAddClick,
                )
                .padding(6.dp),
        )
    }
}
