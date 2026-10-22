package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.ui.components.Avatar
import com.kalary.larkisedosti.ui.components.EmptyState
import com.kalary.larkisedosti.ui.components.GhostButton
import com.kalary.larkisedosti.ui.components.ScreenHeader
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets

@Composable
fun BlockedUsersScreen(onBack: () -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val blocked = app.blockedIds.mapNotNull { app.person(it) }
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets(),
    ) {
        ScreenHeader("Blocked users", onBack)
        if (blocked.isEmpty()) {
            EmptyState(Lucide.Ban, "No one is blocked", "People you block leave Discover, Match, and Chats.")
        } else {
            blocked.forEach { person ->
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = Space.s16, vertical = Space.s12),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Avatar(person)
                    Text(person.name, style = AppType.title, color = colors.textPrimary, modifier = Modifier.weight(1f).padding(horizontal = Space.s12))
                    GhostButton("Unblock", { app.unblock(person.id) })
                }
            }
        }
    }
}
