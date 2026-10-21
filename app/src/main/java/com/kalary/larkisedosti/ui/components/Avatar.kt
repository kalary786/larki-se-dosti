package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.Person
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme

@Composable
fun Avatar(
    person: Person,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showOnline: Boolean = false,
    ring: Boolean = false,
    letter: Boolean = person.letter,
) {
    val colors = LarkiTheme.colors
    Box(modifier.size(size)) {
        Box(
            Modifier
                .fillMaxSize()
                .then(if (ring) Modifier.border(2.dp, colors.primary, CircleShape) else Modifier)
                .padding(if (ring) 2.dp else 0.dp)
                .clip(CircleShape),
        ) {
            if (letter) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(colors.portraits[person.portrait.mod(colors.portraits.size)]),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(person.name.take(1), style = AppType.title, color = colors.onPhoto)
                }
            } else {
                Portrait(seed = person.portrait, modifier = Modifier.fillMaxSize())
            }
        }
        if (showOnline && person.online) {
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(size * 0.28f)
                    .background(colors.surface, CircleShape)
                    .padding(2.dp)
                    .background(colors.online, CircleShape),
            )
        }
    }
}
