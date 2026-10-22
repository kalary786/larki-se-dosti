package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun RateDialog(
    onLater: () -> Unit,
    onRate: () -> Unit,
) {
    val colors = LarkiTheme.colors
    ModalDialog(onDismiss = onLater) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceElevated),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Lucide.Star, contentDescription = null, tint = colors.primary, modifier = Modifier.size(28.dp))
            }
        }
        Text(
            "Rate Larki Se Dosti",
            style = AppType.h2,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = Space.s16),
        )
        Text(
            "If the room feels good, a rating helps more people find it. We only ask once in a while.",
            style = AppType.body,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Space.s8),
        )
        Row(
            Modifier.fillMaxWidth().padding(top = Space.s20),
            horizontalArrangement = Arrangement.spacedBy(Space.s8),
        ) {
            SecondaryButton("Later", onLater, Modifier.weight(1f))
            PrimaryButton("Rate now", onRate, Modifier.weight(1f))
        }
    }
}
