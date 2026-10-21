package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MainTab
import com.kalary.larkisedosti.ui.components.AdSlot
import com.kalary.larkisedosti.ui.components.CoinPill
import com.kalary.larkisedosti.ui.components.CountBadge
import com.kalary.larkisedosti.ui.components.IconCircleButton
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Lucide
import com.kalary.larkisedosti.ui.theme.Radius
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets
import com.kalary.larkisedosti.ui.theme.tag

@Composable
fun MainShell(
    onCoins: () -> Unit,
    onNotifications: () -> Unit,
    onOpenProfile: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onCall: (String, Boolean) -> Unit,
    onEdit: () -> Unit,
    onAccount: () -> Unit,
    onHelp: () -> Unit,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    LaunchedEffect(Unit) { app.maybeShowRate() }
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets(),
    ) {
        TopBar(
            showBell = app.selectedTab == MainTab.Discover,
            onCoins = onCoins,
            onNotifications = onNotifications,
        )
        Box(Modifier.weight(1f)) {
            when (app.selectedTab) {
                MainTab.Discover -> DiscoverScreen(onOpenProfile, onOpenChat, onCall)
                MainTab.Match -> MatchScreen(onOpenChat, onCall)
                MainTab.Chats -> ChatsScreen(onOpenChat)
                MainTab.Friends -> FriendsScreen(onOpenChat)
                MainTab.Profile -> MyProfileScreen(onEdit, onCoins, onAccount, onHelp)
            }
        }
        if (!app.isVip) {
            AdSlot(Modifier.padding(horizontal = Space.s16).padding(bottom = Space.s8))
        }
        BottomNav()
    }
}

@Composable
private fun TopBar(showBell: Boolean, onCoins: () -> Unit, onNotifications: () -> Unit) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.s16, vertical = Space.s8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Larki", style = AppType.h2, color = colors.textPrimary, modifier = Modifier.weight(1f))
        if (showBell) {
            Box(Modifier.padding(end = Space.s8)) {
                IconCircleButton(Lucide.Bell, "Notifications", onNotifications, modifier = Modifier.tag("notifications_button"))
                CountBadge(
                    app.unreadNotes,
                    Modifier.align(Alignment.TopEnd).offset(x = 4.dp, y = (-4).dp),
                )
            }
        }
        CoinPill(app.coins, onCoins)
    }
}

@Composable
private fun BottomNav() {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val shape = RoundedCornerShape(Radius.card)
    Row(
        Modifier
            .padding(horizontal = Space.s16)
            .padding(bottom = Space.s16)
            .shadow(10.dp, shape, ambientColor = colors.shadow, spotColor = colors.shadow)
            .clip(shape)
            .background(colors.surfaceElevated)
            .border(1.dp, colors.border, shape)
            .padding(vertical = Space.s8),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavItem(MainTab.Discover, "Discover", Lucide.Compass, "tab_discover")
        NavItem(MainTab.Match, "Match", Lucide.Radar, "tab_match")
        NavItem(MainTab.Chats, "Chats", Lucide.MessagesSquare, "tab_chats", app.unreadChats)
        NavItem(MainTab.Friends, "Friends", Lucide.Users, "tab_friends")
        NavItem(MainTab.Profile, "Profile", Lucide.CircleUser, "tab_profile")
    }
}

@Composable
private fun NavItem(
    tab: MainTab,
    label: String,
    icon: ImageVector,
    testTag: String,
    badge: Int = 0,
) {
    val colors = LarkiTheme.colors
    val app = LocalAppState.current
    val active = app.selectedTab == tab
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .tag(testTag)
            .clickable { app.selectedTab = tab }
            .padding(horizontal = Space.s4),
    ) {
        Box {
            Icon(icon, contentDescription = label, tint = if (active) colors.primary else colors.textSecondary, modifier = Modifier.size(22.dp))
            CountBadge(badge, Modifier.align(Alignment.TopEnd).offset(x = 8.dp, y = (-6).dp))
        }
        Text(label, style = AppType.caption, color = if (active) colors.primary else colors.textSecondary)
        Box(
            Modifier
                .padding(top = Space.s4)
                .size(4.dp)
                .clip(CircleShape)
                .background(if (active) colors.primary else colors.clear),
        )
    }
}
