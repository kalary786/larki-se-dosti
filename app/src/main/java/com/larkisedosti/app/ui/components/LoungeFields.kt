package com.larkisedosti.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun LoungeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Default,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    onIme: () -> Unit = {},
) {
    val colors = LoungeTheme.colors
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = LoungeType.body.copy(color = colors.textPrimary),
        cursorBrush = SolidColor(colors.primary),
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            capitalization = capitalization,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onIme() },
            onSend = { onIme() },
            onDone = { onIme() },
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(LoungeShape.input)
            .background(colors.surface)
            .border(1.dp, colors.border, LoungeShape.input),
        decorationBox = { inner ->
            Row(
                modifier = Modifier.padding(horizontal = LoungeSpace.s16, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier
                            .padding(end = LoungeSpace.s8)
                            .size(18.dp),
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = colors.textSecondary,
                            style = LoungeType.body,
                        )
                    }
                    inner()
                }
            }
        },
    )
}

@Composable
fun SegmentedControl(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    badges: Map<String, Int> = emptyMap(),
) {
    val colors = LoungeTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(LoungeShape.chip)
            .background(colors.surface)
            .border(1.dp, colors.border, LoungeShape.chip)
            .padding(4.dp),
    ) {
        options.forEach { option ->
            val active = option == selected
            val interaction = remember(option) { MutableInteractionSource() }
            val count = badges[option] ?: 0
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(LoungeShape.chip)
                    .background(if (active) colors.primary else colors.surface.copy(alpha = 0f))
                    .clickable(
                        interactionSource = interaction,
                        indication = ripple(color = colors.onPrimary.copy(alpha = 0.18f)),
                        onClick = { onSelect(option) },
                    )
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = option,
                    color = if (active) colors.onPrimary else colors.textSecondary,
                    style = LoungeType.caption,
                )
                if (count > 0) {
                    Spacer(Modifier.width(6.dp))
                    Badge(count = count)
                }
            }
        }
    }
}
