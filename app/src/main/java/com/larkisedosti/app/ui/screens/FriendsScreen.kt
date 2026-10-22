package com.larkisedosti.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.ui.components.Avatar
import com.larkisedosti.app.ui.components.EmptyState
import com.larkisedosti.app.ui.components.GhostButton
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.PrimaryButton
import com.larkisedosti.app.ui.components.SegmentedControl
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.shell.LocalShellInset
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun FriendsScreen(
    store: AppStore,
    onMessage: (String) -> Unit,
    openRequests: Boolean = false,
) {
    val colors = LoungeTheme.colors
    var tab by rememberSaveable { mutableStateOf(if (openRequests) "Requests" else "Friends") }
    LaunchedEffect(openRequests) {
        if (openRequests) tab = "Requests"
    }
    val friends = store.friendIds.mapNotNull { store.person(it) }.filter { it.id !in store.blockedIds }
    val requests = store.requestIds.mapNotNull { store.person(it) }.filter { it.id !in store.blockedIds }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = LoungeSpace.s16, vertical = LoungeSpace.s12),
    ) {
        SegmentedControl(
            options = listOf("Friends", "Requests"),
            selected = tab,
            onSelect = { tab = it },
            badges = mapOf("Requests" to requests.size),
        )
        Spacer(Modifier.height(LoungeSpace.s16))
        val bottom = LocalShellInset.current
        if (tab == "Friends") {
            if (friends.isEmpty()) {
                EmptyState(
                    title = "No friends yet",
                    message = "When you add someone, they show up here.",
                    icon = AppIcons.Users,
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
                    items(friends, key = { it.id }) { person ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = LoungeSpace.s12, vertical = 12.dp),
                        ) {
                            Avatar(
                                name = person.name,
                                size = 48.dp,
                                online = person.online,
                                containerColor = person.color,
                            )
                            Spacer(Modifier.width(LoungeSpace.s12))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = person.name, color = colors.textPrimary, style = LoungeType.title)
                                Text(
                                    text = if (person.online) "Online" else "Offline",
                                    color = if (person.online) colors.online else colors.textSecondary,
                                    style = LoungeType.caption,
                                )
                            }
                            IconCircleButton(
                                icon = AppIcons.Message,
                                contentDescription = "Message",
                                onClick = { onMessage(person.id) },
                                size = 40.dp,
                            )
                        }
                        if (person.id != friends.last().id) {
                            HorizontalDivider(color = colors.border, modifier = Modifier.padding(start = 72.dp))
                        }
                    }
                }
            }
        } else if (requests.isEmpty()) {
            EmptyState(
                title = "No requests",
                message = "New friend requests will wait here.",
                icon = AppIcons.User,
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
                items(requests, key = { it.id }) { person ->
                    Column(modifier = Modifier.padding(horizontal = LoungeSpace.s12, vertical = 12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Avatar(name = person.name, size = 48.dp, containerColor = person.color)
                            Spacer(Modifier.width(LoungeSpace.s12))
                            Text(
                                text = person.name,
                                color = colors.textPrimary,
                                style = LoungeType.title,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Spacer(Modifier.height(LoungeSpace.s8))
                        Row(horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8)) {
                            PrimaryButton(
                                text = "Accept",
                                onClick = { store.acceptRequest(person.id) },
                                modifier = Modifier.weight(1f),
                            )
                            GhostButton(
                                text = "Decline",
                                onClick = { store.declineRequest(person.id) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                    if (person.id != requests.last().id) {
                        HorizontalDivider(color = colors.border)
                    }
                }
            }
        }
    }
}
