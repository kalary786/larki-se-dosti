package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.ChatFilter
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.ui.components.Avatar
import com.kalary.larkisedosti.ui.components.CountBadge
import com.kalary.larkisedosti.ui.components.EmptyState
import com.kalary.larkisedosti.ui.components.FilterChip
import com.kalary.larkisedosti.ui.components.SearchField
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.tag

@Composable
fun ChatsScreen(onOpenChat: (String) -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val chats = app.visibleChats()
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = Space.s16)
            .tag("screen_chats"),
    ) {
        Text("Chats", style = AppType.h1, color = colors.textPrimary)
        Text("Your live conversations", style = AppType.body, color = colors.textSecondary)
        Row(
            Modifier.padding(top = Space.s16),
            horizontalArrangement = Arrangement.spacedBy(Space.s8),
        ) {
            FilterChip("All", app.chatFilter == ChatFilter.All, { app.chatFilter = ChatFilter.All })
            FilterChip("Unread", app.chatFilter == ChatFilter.Unread, { app.chatFilter = ChatFilter.Unread })
            FilterChip("Online", app.chatFilter == ChatFilter.Online, { app.chatFilter = ChatFilter.Online })
        }
        SearchField(
            value = app.chatQuery,
            onValueChange = { app.chatQuery = it },
            placeholder = "Search conversations",
            modifier = Modifier.padding(vertical = Space.s12),
        )
        if (chats.isEmpty()) {
            EmptyState(
                icon = Lucide.MessagesSquare,
                title = "Nothing in this list",
                body = "Try another filter, or say hello from Discover.",
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Radius.card))
                    .background(colors.surface),
            ) {
                chats.forEachIndexed { index, chat ->
                    val person = app.person(chat.personId) ?: return@forEachIndexed
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .then(if (index == 0) Modifier.tag("chat_row_first") else Modifier)
                            .clickable { onOpenChat(person.id) }
                            .padding(Space.s12),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Avatar(person, showOnline = true, ring = true)
                        Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
                            Text(person.name, style = AppType.title, color = colors.textPrimary, maxLines = 1)
                            Text(
                                chat.preview,
                                style = AppType.body,
                                color = colors.textSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(chat.timeLabel, style = AppType.caption, color = colors.textSecondary)
                            CountBadge(chat.unread, Modifier.padding(top = Space.s4))
                        }
                        Icon(
                            Lucide.ChevronRight,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.padding(start = Space.s8).size(18.dp),
                        )
                    }
                }
            }
        }
    }
}
