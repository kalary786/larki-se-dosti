package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.ui.components.EmptyState
import com.kalary.larkisedosti.ui.components.IconCircleButton
import com.kalary.larkisedosti.ui.components.MenuEntry
import com.kalary.larkisedosti.ui.components.MenuSheet
import com.kalary.larkisedosti.ui.components.Portrait
import com.kalary.larkisedosti.ui.components.ScreenHeader
import com.kalary.larkisedosti.ui.components.SendGiftSheet
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets
import com.kalary.larkisedosti.ui.theme.tag

@Composable
fun ProfileDetailScreen(
    personId: String,
    onBack: () -> Unit,
    onMessage: () -> Unit,
    onVoice: () -> Unit,
    onVideo: () -> Unit,
    onGift: (com.kalary.larkisedosti.data.Gift) -> Unit,
    onCoins: () -> Unit,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val person = app.person(personId)
    var menu by remember { mutableStateOf(false) }
    var gifts by remember { mutableStateOf(false) }
    if (person == null) {
        EmptyState(Lucide.CircleUser, "Profile unavailable", "This person is no longer here.")
        return
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets()
            .tag("screen_profile"),
    ) {
        val pager = rememberPagerState(pageCount = { person.photoCount })
        Box {
            HorizontalPager(state = pager, modifier = Modifier.fillMaxWidth().aspectRatio(0.9f)) { page ->
                Portrait(seed = person.portrait, variant = page, modifier = Modifier.fillMaxSize())
            }
            ScreenHeader(title = "", onBack = onBack, modifier = Modifier.align(Alignment.TopStart))
            IconCircleButton(
                Lucide.EllipsisVertical,
                "More",
                { menu = true },
                modifier = Modifier.align(Alignment.TopEnd).padding(Space.s16),
            )
            Row(
                Modifier.align(Alignment.BottomCenter).padding(bottom = Space.s12),
                horizontalArrangement = Arrangement.spacedBy(Space.s4),
            ) {
                repeat(person.photoCount) { index ->
                    Box(
                        Modifier
                            .size(if (pager.currentPage == index) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (pager.currentPage == index) colors.primary else colors.onPhoto.copy(alpha = 0.55f)),
                    )
                }
            }
        }
        Column(Modifier.padding(Space.s16), verticalArrangement = Arrangement.spacedBy(Space.s8)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${person.name}, ${person.age}", style = AppType.h2, color = colors.textPrimary)
                if (person.verified) {
                    Icon(
                        Lucide.BadgeCheck,
                        contentDescription = "Verified",
                        tint = colors.online,
                        modifier = Modifier.padding(start = Space.s8).size(18.dp),
                    )
                }
            }
            Text(
                "${person.city}, ${person.region}",
                style = AppType.body,
                color = colors.textSecondary,
            )
            Text(
                if (person.online) "Online" else "Offline",
                style = AppType.caption,
                color = if (person.online) colors.online else colors.textSecondary,
            )
            Text(person.bio, style = AppType.body, color = colors.textPrimary)
            ActionGrid(
                friend = app.isFriend(person.id),
                onMessage = onMessage,
                onVoice = onVoice,
                onVideo = onVideo,
                onAdd = { app.addFriend(person.id) },
                onGift = { gifts = true },
                onMore = { menu = true },
            )
        }
    }
    if (menu) {
        MenuSheet(
            title = person.name,
            entries = listOf(
                MenuEntry(Lucide.Flag, "Report") { app.reportFor = person.id },
                MenuEntry(Lucide.Ban, "Block", danger = true) { app.blockFor = person.id },
            ),
            onDismiss = { menu = false },
        )
    }
    if (gifts) {
        SendGiftSheet(
            name = person.name,
            balance = app.coins,
            onClose = { gifts = false },
            onBuyCoins = {
                gifts = false
                onCoins()
            },
            onSend = {
                gifts = false
                onGift(it)
            },
        )
    }
}

@Composable
private fun ActionGrid(
    friend: Boolean,
    onMessage: () -> Unit,
    onVoice: () -> Unit,
    onVideo: () -> Unit,
    onAdd: () -> Unit,
    onGift: () -> Unit,
    onMore: () -> Unit,
) {
    val colors = LarkiTheme.colors
    val items = listOf(
        Triple(Lucide.MessageCircle, "Message", onMessage),
        Triple(Lucide.Phone, "Voice call", onVoice),
        Triple(Lucide.Video, "Video call", onVideo),
        Triple(if (friend) Lucide.UserCheck else Lucide.UserPlus, "Add friend", onAdd),
        Triple(Lucide.Gift, "Send gift", onGift),
        Triple(Lucide.EllipsisVertical, "More", onMore),
    )
    Column(verticalArrangement = Arrangement.spacedBy(Space.s12), modifier = Modifier.padding(top = Space.s8)) {
        items.chunked(3).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { (icon, label, action) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconCircleButton(icon, label, action, tint = colors.primary)
                        Text(label, style = AppType.caption, color = colors.textSecondary, modifier = Modifier.padding(top = Space.s4))
                    }
                }
            }
        }
    }
}
