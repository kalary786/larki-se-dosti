package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.AppNotification
import com.kalary.larkisedosti.data.FriendsSegment
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MainTab
import com.kalary.larkisedosti.data.NotificationKind
import com.kalary.larkisedosti.ui.components.EmptyState
import com.kalary.larkisedosti.ui.components.ScreenHeader
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets
import com.kalary.larkisedosti.ui.theme.tag

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenCall: (String, Boolean) -> Unit,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets()
            .tag("screen_notifications"),
    ) {
        ScreenHeader("Notifications", onBack, subtitle = if (app.notificationsOn) null else "Notifications are off")
        if (app.notifications.isEmpty()) {
            EmptyState(Lucide.Bell, "You're caught up", "Friend requests, messages, and gifts show up here.")
        } else {
            app.notifications.forEach { note ->
                NotificationRow(note) {
                    app.markNotificationRead(note.id)
                    when (note.kind) {
                        NotificationKind.Request -> {
                            app.selectedTab = MainTab.Friends
                            app.friendsSegment = FriendsSegment.Requests
                            onBack()
                        }
                        NotificationKind.Message, NotificationKind.Gift -> onOpenChat(note.personId)
                        NotificationKind.IncomingVoice -> onOpenCall(note.personId, false)
                        NotificationKind.IncomingVideo -> onOpenCall(note.personId, true)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(note: AppNotification, onClick: () -> Unit) {
    val colors = LarkiTheme.colors
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Space.s16, vertical = Space.s12),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (note.unread) colors.primary else colors.clear),
        )
        Column(Modifier.weight(1f).padding(horizontal = Space.s12)) {
            Text(note.title, style = AppType.title, color = colors.textPrimary)
            Text(note.body, style = AppType.body, color = colors.textSecondary)
        }
        Text(note.time, style = AppType.caption, color = colors.textSecondary)
    }
}
