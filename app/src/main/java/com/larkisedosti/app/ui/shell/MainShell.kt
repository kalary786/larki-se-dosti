package com.larkisedosti.app.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.ui.components.AdSlot
import com.larkisedosti.app.ui.components.Badge
import com.larkisedosti.app.ui.components.CoinPill
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.theme.loungeShadow

enum class MainTab(
    val label: String,
    val icon: ImageVector,
) {
    Discover("Discover", AppIcons.Compass),
    Match("Match", AppIcons.Heart),
    Chats("Chats", AppIcons.Message),
    Friends("Friends", AppIcons.Users),
    Profile("Profile", AppIcons.User),
}

val LocalShellInset = staticCompositionLocalOf { 0.dp }

private val ShellNavHeight = 88.dp
private val ShellNavMargin = 16.dp
private val ShellAdHeight = 60.dp
private val ShellAdGap = 8.dp
private val ShellContentGap = 24.dp

@Composable
fun shellBottomInset(showAd: Boolean): Dp {
    val systemNav = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val ad = if (showAd) ShellAdHeight + ShellAdGap else 0.dp
    return ShellNavHeight + ShellNavMargin + systemNav + ad + ShellContentGap
}

@Composable
fun MainShell(
    tab: MainTab,
    onTab: (MainTab) -> Unit,
    isVip: Boolean,
    coins: Int,
    notificationCount: Int,
    unreadChats: Int,
    onNotifications: () -> Unit,
    onAddCoins: () -> Unit,
    content: @Composable () -> Unit,
) {
    val showAd = !isVip
    val inset = shellBottomInset(showAd)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MainTopBar(
                showNotifications = tab == MainTab.Discover,
                notificationCount = notificationCount,
                coinBalance = coins,
                onNotificationsClick = onNotifications,
                onAddCoins = onAddCoins,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                CompositionLocalProvider(LocalShellInset provides inset) {
                    content()
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = ShellNavMargin),
        ) {
            if (showAd) {
                AdSlot(
                    label = "Sponsored",
                    height = ShellAdHeight,
                    modifier = Modifier.padding(horizontal = LoungeSpace.s16),
                )
                Spacer(Modifier.height(ShellAdGap))
            }
            LoungeBottomBar(
                selected = tab,
                unreadChats = unreadChats,
                onSelect = onTab,
                modifier = Modifier
                    .padding(horizontal = LoungeSpace.s16)
                    .height(ShellNavHeight),
            )
        }
    }
}

@Composable
private fun MainTopBar(
    showNotifications: Boolean,
    notificationCount: Int,
    coinBalance: Int,
    onNotificationsClick: () -> Unit,
    onAddCoins: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = LoungeSpace.s16, vertical = LoungeSpace.s8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Wordmark(modifier = Modifier.weight(1f))
        if (showNotifications) {
            Spacer(Modifier.width(LoungeSpace.s8))
            Box {
                IconCircleButton(
                    icon = AppIcons.Bell,
                    contentDescription = "Notifications",
                    onClick = onNotificationsClick,
                )
                Badge(
                    count = notificationCount,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-2).dp),
                )
            }
        }
        Spacer(Modifier.width(LoungeSpace.s8))
        CoinPill(balance = coinBalance, onAddClick = onAddCoins)
    }
}

@Composable
private fun Wordmark(modifier: Modifier = Modifier) {
    val colors = LoungeTheme.colors
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(colors.primary),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "L",
                color = colors.onPrimary,
                style = LoungeType.title,
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Larki Se Dosti",
            color = colors.textPrimary,
            style = LoungeType.title.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun LoungeBottomBar(
    selected: MainTab,
    unreadChats: Int,
    onSelect: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LoungeTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .loungeShadow(LoungeShape.card, colors.isLight)
            .clip(LoungeShape.card)
            .background(colors.surfaceElevated)
            .border(1.dp, colors.border, LoungeShape.card)
            .padding(horizontal = LoungeSpace.s4, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        MainTab.entries.forEach { tab ->
            BottomTab(
                tab = tab,
                selected = tab == selected,
                badgeCount = if (tab == MainTab.Chats) unreadChats else 0,
                onClick = { onSelect(tab) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun BottomTab(
    tab: MainTab,
    selected: Boolean,
    badgeCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LoungeTheme.colors
    val tint = if (selected) colors.primary else colors.textSecondary
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .semantics {
                role = Role.Tab
                this.selected = selected
            }
            .clip(LoungeShape.button)
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = colors.primary.copy(alpha = 0.14f)),
                role = Role.Tab,
                onClick = onClick,
            )
            .padding(vertical = LoungeSpace.s4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(width = 36.dp, height = 26.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(22.dp),
            )
            Badge(
                count = badgeCount,
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }
        Box(
            modifier = Modifier
                .padding(top = LoungeSpace.s4)
                .size(4.dp)
                .clip(CircleShape)
                .background(if (selected) colors.primary else Color.Transparent),
        )
        Text(
            text = tab.label,
            color = tint,
            style = LoungeType.caption,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
