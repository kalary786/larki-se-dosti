package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun EmptyState(
    title: String,
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val colors = LoungeTheme.colors
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LoungeSpace.s24, vertical = LoungeSpace.s32),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.textSecondary,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(colors.surface)
                .padding(22.dp),
        )
        Spacer(Modifier.height(LoungeSpace.s16))
        Text(
            text = title,
            color = colors.textPrimary,
            style = LoungeType.h2,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(
            text = message,
            color = colors.textSecondary,
            style = LoungeType.body,
            textAlign = TextAlign.Center,
        )
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(LoungeSpace.s20))
            PrimaryButton(text = actionLabel, onClick = onAction)
        }
    }
}
