package com.kalary.larkisedosti.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kalary.larkisedosti.data.GateAction
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.MockData
import com.kalary.larkisedosti.data.VideoReward
import com.kalary.larkisedosti.ui.components.ConfirmDialog
import com.kalary.larkisedosti.ui.components.GiftBurstOverlay
import com.kalary.larkisedosti.ui.components.NotEnoughCoinsDialog
import com.kalary.larkisedosti.ui.components.RateDialog
import com.kalary.larkisedosti.ui.components.ReportSheet
import com.kalary.larkisedosti.ui.components.RewardVideoDialog
import com.kalary.larkisedosti.ui.screens.AccountPrivacyScreen
import com.kalary.larkisedosti.ui.screens.BlockedUsersScreen
import com.kalary.larkisedosti.ui.screens.CallScreen
import com.kalary.larkisedosti.ui.screens.ChatScreen
import com.kalary.larkisedosti.ui.screens.CoinStoreScreen
import com.kalary.larkisedosti.ui.screens.EditProfileScreen
import com.kalary.larkisedosti.ui.screens.HelpScreen
import com.kalary.larkisedosti.ui.screens.LegalScreen
import com.kalary.larkisedosti.ui.screens.MainShell
import com.kalary.larkisedosti.ui.screens.NotificationsScreen
import com.kalary.larkisedosti.ui.screens.OnboardingScreen
import com.kalary.larkisedosti.ui.screens.ProfileDetailScreen
import com.kalary.larkisedosti.ui.screens.SplashScreen
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Radius
import androidx.compose.foundation.shape.RoundedCornerShape
import com.kalary.larkisedosti.ui.theme.Space

val LocalNav = staticCompositionLocalOf<NavHostController> { error("Navigation is missing") }

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val app = LocalAppState.current
    val colors = LarkiTheme.colors
    val snackbarHost = remember { SnackbarHostState() }
    LaunchedEffect(app.snackbar) {
        val message = app.snackbar ?: return@LaunchedEffect
        snackbarHost.showSnackbar(message)
        app.snackbar = null
    }
    CompositionLocalProvider(LocalNav provides nav) {
        Box(Modifier.fillMaxSize()) {
            NavHost(navController = nav, startDestination = Routes.Splash) {
                composable(Routes.Splash) {
                    SplashScreen { destination ->
                        nav.navigate(destination) { popUpTo(Routes.Splash) { inclusive = true } }
                    }
                }
                composable(Routes.Onboarding) {
                    OnboardingScreen(
                        onDone = {
                            nav.navigate(Routes.Main) { popUpTo(Routes.Onboarding) { inclusive = true } }
                        },
                        onLegal = { nav.navigate(Routes.legal(it)) },
                    )
                }
                composable(Routes.Main) {
                    MainShell(
                        onCoins = { nav.navigate(Routes.Coins) },
                        onNotifications = { nav.navigate(Routes.Notifications) },
                        onOpenProfile = { nav.navigate(Routes.profile(it)) },
                        onOpenChat = { nav.navigate(Routes.chat(it)) },
                        onCall = { id, video -> startOutgoingCall(app, nav, id, video) },
                        onEdit = { nav.navigate(Routes.Edit) },
                        onAccount = { nav.navigate(Routes.Account) },
                        onHelp = { nav.navigate(Routes.Help) },
                    )
                }
                composable(
                    Routes.Profile,
                    arguments = listOf(navArgument("personId") { type = NavType.StringType }),
                ) { entry ->
                    val id = entry.arguments?.getString("personId").orEmpty()
                    ProfileDetailScreen(
                        personId = id,
                        onBack = { nav.popBackStack() },
                        onMessage = { nav.navigate(Routes.chat(app.baseId(id))) },
                        onVoice = { startOutgoingCall(app, nav, id, video = false) },
                        onVideo = { startOutgoingCall(app, nav, id, video = true) },
                        onGift = { requestGift(app, id, it) },
                        onCoins = { nav.navigate(Routes.Coins) },
                    )
                }
                composable(
                    Routes.Chat,
                    arguments = listOf(navArgument("personId") { type = NavType.StringType }),
                ) { entry ->
                    val id = entry.arguments?.getString("personId").orEmpty()
                    ChatScreen(
                        personId = id,
                        onBack = { nav.popBackStack() },
                        onVoice = { startOutgoingCall(app, nav, id, false) },
                        onVideo = { startOutgoingCall(app, nav, id, true) },
                        onGift = { requestGift(app, id, it) },
                        onCoins = { nav.navigate(Routes.Coins) },
                    )
                }
                composable(
                    Routes.Call,
                    arguments = listOf(
                        navArgument("personId") { type = NavType.StringType },
                        navArgument("video") { type = NavType.IntType },
                        navArgument("incoming") { type = NavType.IntType },
                    ),
                ) { entry ->
                    CallScreen(
                        personId = entry.arguments?.getString("personId").orEmpty(),
                        video = entry.arguments?.getInt("video") == 1,
                        incoming = entry.arguments?.getInt("incoming") == 1,
                        onEnd = { nav.popBackStack() },
                    )
                }
                composable(Routes.Notifications) {
                    NotificationsScreen(
                        onBack = { nav.popBackStack() },
                        onOpenChat = { id ->
                            nav.popBackStack()
                            nav.navigate(Routes.chat(id))
                        },
                        onOpenCall = { id, video ->
                            nav.popBackStack()
                            nav.navigate(Routes.call(id, video, incoming = true))
                        },
                    )
                }
                composable(Routes.Coins) { CoinStoreScreen { nav.popBackStack() } }
                composable(Routes.Edit) { EditProfileScreen { nav.popBackStack() } }
                composable(Routes.Account) {
                    AccountPrivacyScreen(
                        onBack = { nav.popBackStack() },
                        onBlocked = { nav.navigate(Routes.Blocked) },
                        onLegal = { nav.navigate(Routes.legal(it)) },
                    )
                }
                composable(Routes.Blocked) { BlockedUsersScreen { nav.popBackStack() } }
                composable(Routes.Help) {
                    HelpScreen(onBack = { nav.popBackStack() }, onLegal = { nav.navigate(Routes.legal(it)) })
                }
                composable(
                    Routes.Legal,
                    arguments = listOf(navArgument("page") { type = NavType.StringType }),
                ) { entry ->
                    LegalScreen(entry.arguments?.getString("page").orEmpty()) { nav.popBackStack() }
                }
            }
            SnackbarHost(
                hostState = snackbarHost,
                modifier = Modifier.align(Alignment.BottomCenter).padding(Space.s16),
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = colors.surfaceElevated,
                    contentColor = colors.textPrimary,
                    shape = RoundedCornerShape(Radius.button),
                )
            }
            app.gate?.let { gate ->
                NotEnoughCoinsDialog(
                    balance = app.coins,
                    need = gate.need,
                    pill = gate.pill,
                    onBuy = {
                        app.gate = null
                        if (nav.currentDestination?.route != Routes.Coins) nav.navigate(Routes.Coins)
                    },
                    onWatch = { app.videoReward = VideoReward.FreeAction },
                    onDismiss = { app.gate = null },
                )
            }
            if (app.rateVisible) {
                RateDialog(
                    onLater = { app.dismissRate(rated = false) },
                    onRate = { app.dismissRate(rated = true) },
                )
            }
            app.reportFor?.let { id ->
                val name = app.person(id)?.name ?: "this person"
                ReportSheet(
                    name = name,
                    onDismiss = { app.reportFor = null },
                    onSubmit = { _, _ ->
                        app.reportFor = null
                        app.snackbar = "Report sent. Thank you."
                    },
                )
            }
            app.blockFor?.let { id ->
                val name = app.person(id)?.name ?: "this person"
                ConfirmDialog(
                    title = "Block $name?",
                    body = "They will disappear from Discover, Match, and Chats. You can unblock them from Account & Privacy.",
                    confirmLabel = "Block",
                    dismissLabel = "Cancel",
                    danger = true,
                    onConfirm = {
                        app.block(id)
                        app.blockFor = null
                        val route = nav.currentDestination?.route.orEmpty()
                        if (route.startsWith("profile") || route.startsWith("chat") || route.startsWith("call")) {
                            nav.popBackStack()
                        }
                    },
                    onDismiss = { app.blockFor = null },
                )
            }
            app.deleteChatFor?.let { id ->
                ConfirmDialog(
                    title = "Delete this chat?",
                    body = "Messages on this device will be removed.",
                    confirmLabel = "Delete",
                    dismissLabel = "Keep chat",
                    danger = true,
                    onConfirm = {
                        app.deleteChat(id)
                        app.deleteChatFor = null
                        nav.popBackStack()
                    },
                    onDismiss = { app.deleteChatFor = null },
                )
            }
            if (app.confirmDeleteAccount) {
                ConfirmDialog(
                    title = "Delete your account?",
                    body = "Your name, photos, and local profile are removed from this device. You will go back to sign-up.",
                    confirmLabel = "Delete account",
                    dismissLabel = "Cancel",
                    danger = true,
                    onConfirm = {
                        app.deleteAccount()
                        nav.navigate(Routes.Onboarding) {
                            popUpTo(nav.graph.id) { inclusive = true }
                        }
                    },
                    onDismiss = { app.confirmDeleteAccount = false },
                )
            }
            app.videoReward?.let {
                RewardVideoDialog {
                    val reward = app.videoReward
                    val action = app.gate?.action
                    app.videoReward = null
                    when (reward) {
                        VideoReward.Coins -> app.grantVideoCoins()
                        VideoReward.FreeAction -> {
                            app.gate = null
                            when (action) {
                                is GateAction.Call -> {
                                    if (action.incoming) app.freePass = true
                                    else nav.navigate(Routes.call(action.personId, action.video, incoming = false))
                                }
                                is GateAction.GiftSend -> app.deliverGift(action.personId, MockData.gift(action.giftId))
                                null -> Unit
                            }
                        }
                        null -> Unit
                    }
                }
            }
            app.burstGiftId?.let { id ->
                GiftBurstOverlay(MockData.gift(id)) { app.burstGiftId = null }
            }
        }
    }
}
