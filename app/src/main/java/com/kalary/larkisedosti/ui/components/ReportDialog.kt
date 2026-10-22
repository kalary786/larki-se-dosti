package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

private val Reasons = listOf(
    "Spam or scam",
    "Harassment",
    "Fake profile",
    "Inappropriate photos",
    "Someone under 18",
    "Something else",
)

@Composable
fun ReportSheet(
    name: String,
    onDismiss: () -> Unit,
    onSubmit: (reason: String, note: String) -> Unit,
) {
    val colors = LarkiTheme.colors
    var reason by remember { mutableStateOf<String?>(null) }
    var note by remember { mutableStateOf("") }
    AppBottomSheet(onDismiss) {
        Column(Modifier.padding(horizontal = Space.s16).padding(bottom = Space.s24)) {
            Text("Report $name", style = AppType.h2, color = colors.textPrimary)
            Text(
                "Tell us what happened. Reports stay with the safety team.",
                style = AppType.body,
                color = colors.textSecondary,
                modifier = Modifier.padding(top = Space.s8, bottom = Space.s16),
            )
            Reasons.forEach { item ->
                val selected = reason == item
                Text(
                    item,
                    style = AppType.body,
                    color = if (selected) colors.onPrimary else colors.textPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Space.s8)
                        .background(
                            if (selected) colors.primary else colors.surfaceElevated,
                            RoundedCornerShape(Radius.input),
                        )
                        .border(1.dp, if (selected) colors.primary else colors.border, RoundedCornerShape(Radius.input))
                        .clickable { reason = item }
                        .padding(Space.s12),
                )
            }
            OutlinedTextField(
                value = note,
                onValueChange = { note = it.take(240) },
                modifier = Modifier.fillMaxWidth().padding(top = Space.s8),
                placeholder = { Text("Add a note (optional)", color = colors.textSecondary) },
                textStyle = AppType.body.copy(color = colors.textPrimary),
                shape = RoundedCornerShape(Radius.input),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.border,
                    cursorColor = colors.primary,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface,
                ),
            )
            PrimaryButton(
                text = "Submit report",
                onClick = { reason?.let { onSubmit(it, note.trim()) } },
                enabled = reason != null,
                modifier = Modifier.fillMaxWidth().padding(top = Space.s16),
            )
        }
    }
}
