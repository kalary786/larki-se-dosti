package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.theme.loungeShadow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCard(
    name: String,
    age: Int,
    subtitle: String,
    modifier: Modifier = Modifier,
    online: Boolean = false,
    tags: List<String> = emptyList(),
    onClick: (() -> Unit)? = null,
) {
    val colors = LoungeTheme.colors
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .loungeShadow(LoungeShape.card, colors.isLight)
            .clip(LoungeShape.card)
            .background(colors.surface)
            .border(1.dp, colors.border, LoungeShape.card)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interaction,
                        indication = ripple(color = colors.textPrimary.copy(alpha = 0.08f)),
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .padding(LoungeSpace.s16),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(
                name = name,
                size = 64.dp,
                online = online,
                ring = true,
                dotRing = colors.surface,
            )
            Spacer(Modifier.width(LoungeSpace.s12))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$name, $age",
                    color = colors.textPrimary,
                    style = LoungeType.title,
                )
                Spacer(Modifier.height(LoungeSpace.s4))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = AppIcons.MapPin,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(Modifier.width(LoungeSpace.s4))
                    Text(
                        text = subtitle,
                        color = colors.textSecondary,
                        style = LoungeType.caption,
                    )
                }
            }
        }
        if (tags.isNotEmpty()) {
            Spacer(Modifier.height(LoungeSpace.s12))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
                verticalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
            ) {
                tags.forEach { tag ->
                    Text(
                        text = tag,
                        color = colors.textSecondary,
                        style = LoungeType.caption,
                        modifier = Modifier
                            .clip(LoungeShape.chip)
                            .background(colors.surfaceElevated)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    )
                }
            }
        }
    }
}
