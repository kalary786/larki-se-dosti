package com.larkisedosti.app.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun Avatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    online: Boolean = false,
    ring: Boolean = false,
    image: Painter? = null,
    dotRing: Color? = null,
    containerColor: Color = Color.Unspecified,
) {
    val colors = LoungeTheme.colors
    val dot = (size * 0.28f).coerceIn(10.dp, 16.dp)
    Box(modifier = modifier.size(size)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (ring) {
                        Modifier
                            .border(2.dp, colors.primary, CircleShape)
                            .padding(3.dp)
                    } else {
                        Modifier
                    },
                )
                .clip(CircleShape)
                .background(if (containerColor == Color.Unspecified) colors.surfaceElevated else containerColor),
            contentAlignment = Alignment.Center,
        ) {
            if (image != null) {
                Image(
                    painter = image,
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Text(
                    text = initials(name),
                    color = colors.textPrimary,
                    style = LoungeType.title.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = (size.value * 0.34f).sp,
                    ),
                )
            }
        }
        if (online) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(dot)
                    .background(dotRing ?: colors.background, CircleShape)
                    .padding(2.dp)
                    .background(colors.online, CircleShape),
            )
        }
    }
}

private fun initials(name: String): String {
    return name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.take(1).uppercase() }
        .ifEmpty { "?" }
}
