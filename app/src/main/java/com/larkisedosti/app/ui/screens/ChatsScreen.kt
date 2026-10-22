package com.larkisedosti.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.Conversation
import com.larkisedosti.app.ui.components.Avatar
import com.larkisedosti.app.ui.components.Badge
import com.larkisedosti.app.ui.components.ChatSkeletonRow
import com.larkisedosti.app.ui.components.EmptyState
import com.larkisedosti.app.ui.components.FilterChip
import com.larkisedosti.app.ui.components.LoungeTextField
import com.larkisedosti.app.ui.components.rememberSkeletonPulse
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.shell.LocalShellInset
import kotlinx.coroutines.delay

@Composable
fun ChatsScreen(
    store: AppStore,
    onOpenChat: (String) -> Unit,
) {
    val colors = LoungeTheme.colors
    var filter by rememberSaveable { mutableStateOf("All") }
    var query by rememberSaveable { mutableStateOf("") }
    var ready by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!ready) {
            delay(700)
            ready = true
        }
    }
    val rows = store.conversations.mapNotNull { conversation ->
        val person = store.person(conversation.personId) ?: return@mapNotNull null
        if (person.id in store.blockedIds) return@mapNotNull null
        conversation to person
    }.filter { (conversation, person) ->
        val last = conversation.messages.lastOrNull()
        val haystack = "${person.name} ${last?.text.orEmpty()}"
        val matchesQuery = query.isBlank() || haystack.contains(query, ignoreCase = true)
        val matchesFilter = when (filter) {
            "Unread" -> conversation.unread > 0
            "Online" -> person.online
            else -> true
        }
        matchesQuery && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = LoungeSpace.s16),
    ) {
        Spacer(Modifier.height(LoungeSpace.s8))
        Text(text = "Chats", color = colors.textPrimary, style = LoungeType.h2)
        Text(
            text = "Your live conversations",
            color = colors.textSecondary,
            style = LoungeType.body,
        )
        Spacer(Modifier.height(LoungeSpace.s12))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
        ) {
            listOf("All", "Unread", "Online").forEach { label ->
                FilterChip(label = label, selected = filter == label, onClick = { filter = label })
            }
        }
        Spacer(Modifier.height(LoungeSpace.s12))
        LoungeTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = "Search conversations",
            leadingIcon = AppIcons.Search,
        )
        Spacer(Modifier.height(LoungeSpace.s16))
        val bottom = LocalShellInset.current
        if (!ready) {
            val pulse = rememberSkeletonPulse()
            Column(
                modifier = Modifier
                    .padding(bottom = bottom)
                    .fillMaxWidth()
                    .clip(LoungeShape.card)
                    .background(colors.surface)
                    .border(1.dp, colors.border, LoungeShape.card),
            ) {
                repeat(5) { index ->
                    ChatSkeletonRow(pulse)
                    if (index < 4) {
                        HorizontalDivider(color = colors.border, modifier = Modifier.padding(start = 72.dp))
                    }
                }
            }
        } else if (rows.isEmpty()) {
            EmptyState(
                title = "No chats yet",
                message = "When you message someone, the conversation will land here.",
                icon = AppIcons.Inbox,
                modifier = Modifier.padding(bottom = bottom),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = bottom)
                    .fillMaxWidth()
                    .clip(LoungeShape.card)
                    .background(colors.surface)
                    .border(1.dp, colors.border, LoungeShape.card),
            ) {
                items(rows, key = { it.first.personId }) { (conversation, person) ->
                    ChatRow(conversation, person.name, person.online, person.color, person.id in store.favorites) {
                        onOpenChat(person.id)
                    }
                    if (conversation != rows.last().first) {
                        HorizontalDivider(color = colors.border, modifier = Modifier.padding(start = 72.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatRow(
    conversation: Conversation,
    name: String,
    online: Boolean,
    color: androidx.compose.ui.graphics.Color,
    saved: Boolean,
    onClick: () -> Unit,
) {
    val colors = LoungeTheme.colors
    val last = conversation.messages.lastOrNull()
    val preview = when {
        last?.gift != null -> "Gift · ${last.gift.title}"
        else -> last?.text.orEmpty()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = LoungeSpace.s12, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(
            name = name,
            size = 48.dp,
            online = online,
            ring = saved || online,
            containerColor = color,
            dotRing = colors.surface,
        )
        Spacer(Modifier.width(LoungeSpace.s12))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = colors.textPrimary,
                style = LoungeType.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = preview,
                color = colors.textSecondary,
                style = LoungeType.body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = conversation.timeLabel, color = colors.textSecondary, style = LoungeType.caption)
            Spacer(Modifier.height(6.dp))
            Badge(count = conversation.unread)
        }
        Icon(
            imageVector = AppIcons.ChevronRight,
            contentDescription = null,
            tint = colors.textSecondary,
            modifier = Modifier
                .padding(start = 6.dp)
                .size(16.dp),
        )
    }
}
