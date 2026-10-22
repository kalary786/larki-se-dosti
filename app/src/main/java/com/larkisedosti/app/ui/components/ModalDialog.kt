package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.theme.appear

@Composable
fun ModalDialog(
    visible: Boolean,
    title: String,
    body: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissLabel: String = "Cancel",
    destructive: Boolean = false,
) {
    if (!visible) return
    val colors = LoungeTheme.colors
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .appear()
                .padding(horizontal = LoungeSpace.s24)
                .fillMaxWidth()
                .clip(LoungeShape.card)
                .background(colors.surfaceElevated)
                .padding(LoungeSpace.s24),
        ) {
            Text(text = title, color = colors.textPrimary, style = LoungeType.h2)
            Spacer(Modifier.height(LoungeSpace.s8))
            Text(text = body, color = colors.textSecondary, style = LoungeType.body)
            Spacer(Modifier.height(LoungeSpace.s24))
            Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
                GhostButton(
                    text = dismissLabel,
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                )
                if (destructive) {
                    LoungeButton(
                        text = confirmLabel,
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        enabled = true,
                        icon = null,
                        container = colors.danger,
                        content = colors.onPrimary,
                        border = null,
                    )
                } else {
                    PrimaryButton(
                        text = confirmLabel,
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}
