package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kalary.larkisedosti.data.FriendsSegment
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.ui.components.Avatar
import com.kalary.larkisedosti.ui.components.EmptyState
import com.kalary.larkisedosti.ui.components.FilterChip
import com.kalary.larkisedosti.ui.components.GhostButton
import com.kalary.larkisedosti.ui.components.PrimaryButton
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.tag

@Composable
fun FriendsScreen(onOpenChat: (String) -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = Space.s16)
            .tag("screen_friends"),
    ) {
        Text("Friends", style = AppType.h1, color = colors.textPrimary)
        Row(
            Modifier.padding(top = Space.s16),
            horizontalArrangement = Arrangement.spacedBy(Space.s8),
        ) {
            FilterChip("Friends", app.friendsSegment == FriendsSegment.Friends, { app.friendsSegment = FriendsSegment.Friends })
            FilterChip("Requests", app.friendsSegment == FriendsSegment.Requests, { app.friendsSegment = FriendsSegment.Requests })
        }
        if (app.friendsSegment == FriendsSegment.Friends) {
            val friends = app.friendIds.mapNotNull { app.person(it) }.filter { !app.isBlocked(it.id) }
            if (friends.isEmpty()) {
                EmptyState(Lucide.Users, "No friends yet", "Add someone from Discover.")
            } else {
                friends.forEach { person ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = Space.s12),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Avatar(person, showOnline = true)
                        Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
                            Text(person.name, style = AppType.title, color = colors.textPrimary)
                            Text(
                                if (person.online) "Online" else "Offline",
                                style = AppType.caption,
                                color = if (person.online) colors.online else colors.textSecondary,
                            )
                        }
                        PrimaryButton("Message", { onOpenChat(person.id) })
                    }
                }
            }
        } else {
            val requests = app.requestIds.mapNotNull { app.person(it) }.filter { !app.isBlocked(it.id) }
            if (requests.isEmpty()) {
                EmptyState(Lucide.UserPlus, "No requests right now", "New requests will show up here.")
            } else {
                requests.forEach { person ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = Space.s12),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Avatar(person)
                        Text(
                            person.name,
                            style = AppType.title,
                            color = colors.textPrimary,
                            modifier = Modifier.weight(1f).padding(horizontal = Space.s12),
                        )
                        PrimaryButton("Accept", { app.acceptRequest(person.id) })
                        GhostButton("Decline", { app.declineRequest(person.id) })
                    }
                }
            }
        }
    }
}
