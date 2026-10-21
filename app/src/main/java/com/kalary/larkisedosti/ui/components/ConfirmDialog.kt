package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun ConfirmDialog(
    title: String,
    body: String,
    confirmLabel: String,
    dismissLabel: String,
    danger: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = LarkiTheme.colors
    ModalDialog(onDismiss = onDismiss) {
        Text(title, style = AppType.h2, color = colors.textPrimary)
        Text(body, style = AppType.body, color = colors.textSecondary, modifier = Modifier.paddingTop())
        Row(
            Modifier.fillMaxWidth().paddingTop(),
            horizontalArrangement = Arrangement.spacedBy(Space.s8),
        ) {
            GhostButton(dismissLabel, onDismiss, Modifier.weight(1f))
            PrimaryButton(
                confirmLabel,
                onConfirm,
                Modifier.weight(1f),
                tone = if (danger) ButtonTone.Danger else ButtonTone.Accent,
            )
        }
    }
}

private fun Modifier.paddingTop(): Modifier = padding(top = Space.s12)
