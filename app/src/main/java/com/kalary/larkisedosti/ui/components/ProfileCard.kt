package com.kalary.larkisedosti.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.Person
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space

@Composable
fun ProfileCard(
    person: Person,
    favorite: Boolean,
    friend: Boolean,
    onOpen: () -> Unit,
    onFavorite: () -> Unit,
    onMessage: () -> Unit,
    onVideo: () -> Unit,
    onAddFriend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LarkiTheme.colors
    val shape = RoundedCornerShape(Radius.card)
    Box(
        modifier = modifier
            .shadow(8.dp, shape, clip = false, ambientColor = colors.shadow, spotColor = colors.shadow)
            .clip(shape)
            .clickable(onClick = onOpen),
    ) {
        Portrait(seed = person.portrait, modifier = Modifier.fillMaxSize())
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.62f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(colors.clear, colors.scrim.copy(alpha = 0.92f)),
                    ),
                ),
        )
        if (person.online) {
            Row(
                Modifier
                    .align(Alignment.TopStart)
                    .padding(Space.s8)
                    .clip(CircleShape)
                    .background(colors.overlay)
                    .padding(horizontal = Space.s8, vertical = Space.s4),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Space.s4),
            ) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(colors.online))
                Text("Online", style = AppType.caption, color = colors.onPhoto)
            }
        }
        IconCircleButton(
            icon = Lucide.Heart,
            contentDescription = "Favorite",
            onClick = onFavorite,
            tint = if (favorite) colors.danger else colors.onPhoto,
            background = colors.overlay,
            size = 32.dp,
            iconSize = 16.dp,
            modifier = Modifier.align(Alignment.TopEnd).padding(Space.s8),
        )
        Column(
            Modifier
                .align(Alignment.BottomStart)
                .padding(Space.s8),
            verticalArrangement = Arrangement.spacedBy(Space.s8),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    person.name,
                    style = AppType.title,
                    color = colors.onPhoto,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Spacer(Modifier.size(Space.s4))
                Text(person.age.toString(), style = AppType.caption, color = colors.onPhoto)
                if (person.verified) {
                    Spacer(Modifier.size(Space.s4))
                    Icon(Lucide.BadgeCheck, contentDescription = "Verified", tint = colors.online, modifier = Modifier.size(14.dp))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Space.s4)) {
                Icon(Lucide.MapPin, contentDescription = null, tint = colors.onPhoto, modifier = Modifier.size(12.dp))
                Text(
                    "${person.city}, ${person.country}",
                    style = AppType.caption,
                    color = colors.onPhoto,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Space.s8)) {
                IconCircleButton(Lucide.MessageCircle, "Message", onMessage, tint = colors.onPhoto, background = colors.overlay, size = 32.dp, iconSize = 15.dp)
                IconCircleButton(Lucide.Video, "Video call", onVideo, tint = colors.onPhoto, background = colors.overlay, size = 32.dp, iconSize = 15.dp)
                IconCircleButton(
                    if (friend) Lucide.UserCheck else Lucide.UserPlus,
                    "Add friend",
                    onAddFriend,
                    tint = colors.onPhoto,
                    background = colors.overlay,
                    size = 32.dp,
                    iconSize = 15.dp,
                )
            }
        }
    }
}
