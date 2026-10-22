package com.larkisedosti.app

import android.os.SystemClock
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.larkisedosti.app.data.AccountGate
import com.larkisedosti.app.data.FirebaseAccount
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.ui.components.NotEnoughCoinsModal
import com.larkisedosti.app.ui.components.RateAppModal
import com.larkisedosti.app.ui.components.RatePrompt
import com.larkisedosti.app.ui.screens.CallScreen
import com.larkisedosti.app.ui.screens.ChatScreen
import com.larkisedosti.app.ui.screens.ChatsScreen
import com.larkisedosti.app.ui.screens.CoinStoreScreen
import com.larkisedosti.app.ui.screens.DiscoverScreen
import com.larkisedosti.app.ui.screens.EditProfileScreen
import com.larkisedosti.app.ui.screens.FriendsScreen
import com.larkisedosti.app.ui.screens.HelpScreen
import com.larkisedosti.app.ui.screens.MatchScreen
import com.larkisedosti.app.ui.screens.MyProfileScreen
import com.larkisedosti.app.ui.screens.NotificationsScreen
import com.larkisedosti.app.ui.screens.OnboardingScreen
import com.larkisedosti.app.ui.screens.PolicyPageScreen
import com.larkisedosti.app.ui.screens.PrivacyScreen
import com.larkisedosti.app.ui.screens.ProfileDetailScreen
import com.larkisedosti.app.ui.screens.SplashScreen
import com.larkisedosti.app.ui.shell.MainShell
import com.larkisedosti.app.ui.shell.MainTab
import com.larkisedosti.app.ui.theme.LoungeMotion
import kotlinx.coroutines.delay

@Composable
fun LarkiApp() {
    val context = LocalContext.current
    val account = remember { FirebaseAccount(context) }
    val store = remember { AppStore(account) }
    val stack = remember { mutableStateListOf("splash") }
    var tab by remember { mutableStateOf(MainTab.Discover) }
    var focusRequests by remember { mutableStateOf(false) }
    var needCoins by remember { mutableStateOf(false) }
    var pendingCall by remember { mutableStateOf<PendingCall?>(null) }
    var sessionCounted by rememberSaveable { mutableStateOf(false) }
    var showRate by remember { mutableStateOf(false) }
    var splashError by remember { mutableStateOf<String?>(null) }
    var splashAttempt by remember { mutableIntStateOf(0) }
    val top = stack.last()

    DisposableEffect(account) {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                account.setOnline(true)
            }

            override fun onStop(owner: LifecycleOwner) {
                account.setOnline(false)
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
        onDispose { ProcessLifecycleOwner.get().lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(splashAttempt) {
        splashError = null
        val started = SystemClock.elapsedRealtime()
        var failure: String? = null
        val gate = try {
            account.enter()
        } catch (error: Exception) {
            failure = error.message
            null
        }
        val remaining = 1500L - (SystemClock.elapsedRealtime() - started)
        if (remaining > 0) delay(remaining)
        when (gate) {
            is AccountGate.Discover -> {
                store.restore(gate.profile)
                account.setOnline(true)
                tab = MainTab.Discover
                stack.clear()
                stack.add("main")
            }
            AccountGate.Onboarding -> {
                stack.clear()
                stack.add("onboard")
            }
            null -> splashError = failure ?: "Couldn't open your account. Check your connection and tap to try again."
        }
    }

    LaunchedEffect(store.signedIn) {
        if (store.signedIn && !sessionCounted) {
            sessionCounted = true
            showRate = RatePrompt.recordSession(context)
        }
    }

    fun push(route: String) {
        stack.add(route)
    }

    fun pop() {
        if (stack.size > 1) stack.removeAt(stack.lastIndex)
    }

    fun home() {
        stack.clear()
        stack.add("main")
    }

    fun openCall(id: String, video: Boolean, incoming: Boolean = false, free: Boolean = false) {
        if (!free && !incoming && !store.canAffordCall()) {
            pendingCall = PendingCall(id, video, incoming)
            needCoins = true
            return
        }
        val kind = if (video) "v" else "a"
        val direction = if (incoming) "in" else "out"
        val charge = if (free) "free" else "paid"
        push("call:$id:$kind:$direction:$charge")
    }

    BackHandler(enabled = top != "splash" && top != "onboard" && top != "main") {
        pop()
    }

    AnimatedContent(
        targetState = top,
        transitionSpec = {
            (fadeIn(tween(LoungeMotion.Standard)) + scaleIn(initialScale = 0.98f, animationSpec = tween(LoungeMotion.Standard)))
                .togetherWith(fadeOut(tween(LoungeMotion.Fast)))
        },
        label = "screen",
    ) { route ->
    when {
        route == "splash" -> SplashScreen(
            message = splashError,
            onRetry = { splashAttempt += 1 },
        )
        route == "onboard" -> OnboardingScreen(store) {
            tab = MainTab.Discover
            home()
        }
        route == "main" -> MainShell(
            tab = tab,
            onTab = {
                tab = it
                if (it != MainTab.Friends) focusRequests = false
            },
            isVip = store.boosted,
            coins = store.coins,
            notificationCount = if (store.notificationsOn) store.unreadNotices else 0,
            unreadChats = store.unreadChats,
            onNotifications = { push("notes") },
            onAddCoins = { push("coins") },
        ) {
            AnimatedContent(
                targetState = tab,
                transitionSpec = {
                    fadeIn(tween(LoungeMotion.Standard)) togetherWith fadeOut(tween(LoungeMotion.Fast))
                },
                label = "tab",
            ) { currentTab ->
            when (currentTab) {
                MainTab.Discover -> DiscoverScreen(
                    store = store,
                    onOpenProfile = { push("profile:$it") },
                    onMessage = { push("chat:$it") },
                    onVideo = { openCall(it, video = true) },
                )
                MainTab.Match -> MatchScreen(
                    store = store,
                    onOpenChat = { push("chat:$it") },
                    onCall = { openCall(it, video = false) },
                )
                MainTab.Chats -> ChatsScreen(store = store, onOpenChat = { push("chat:$it") })
                MainTab.Friends -> FriendsScreen(
                    store = store,
                    onMessage = { push("chat:$it") },
                    openRequests = focusRequests,
                )
                MainTab.Profile -> MyProfileScreen(
                    store = store,
                    onEdit = { push("edit") },
                    onCoins = { push("coins") },
                    onFriends = { tab = MainTab.Friends },
                    onPrivacy = { push("privacy") },
                    onHelp = { push("help") },
                )
            }
            }
        }
        route.startsWith("profile:") -> ProfileDetailScreen(
            store = store,
            personId = route.removePrefix("profile:"),
            onBack = { pop() },
            onMessage = { push("chat:${route.removePrefix("profile:")}") },
            onVoice = { openCall(route.removePrefix("profile:"), video = false) },
            onVideo = { openCall(route.removePrefix("profile:"), video = true) },
            onCoins = { push("coins") },
        )
        route.startsWith("chat:") -> {
            val id = route.removePrefix("chat:")
            ChatScreen(
                store = store,
                personId = id,
                onBack = { pop() },
                onVoice = { openCall(id, video = false) },
                onVideo = { openCall(id, video = true) },
                onCoins = { push("coins") },
            )
        }
        route == "coins" -> CoinStoreScreen(store = store, onBack = { pop() })
        route == "notes" -> NotificationsScreen(
            store = store,
            onBack = { pop() },
            onOpen = { notice ->
                when (notice.kind) {
                    "request" -> {
                        tab = MainTab.Friends
                        focusRequests = true
                        home()
                    }
                    "message", "gift" -> notice.personId?.let { push("chat:$it") }
                    "call" -> notice.personId?.let { openCall(it, video = false, incoming = true) }
                }
            },
        )
        route.startsWith("call:") -> {
            val parts = route.split(":")
            val id = parts.getOrNull(1).orEmpty()
            val person = store.person(id)
            if (person != null) {
                CallScreen(
                    store = store,
                    person = person,
                    video = parts.getOrNull(2) == "v",
                    incoming = parts.getOrNull(3) == "in",
                    waived = parts.getOrNull(4) == "free",
                    onBack = { pop() },
                    onCoins = {
                        pop()
                        push("coins")
                    },
                )
            }
        }
        route == "edit" -> EditProfileScreen(store = store, onBack = { pop() })
        route == "privacy" -> PrivacyScreen(
            store = store,
            onBack = { pop() },
            onOpenPage = { push("page:$it") },
            onDeleted = {
                tab = MainTab.Discover
                stack.clear()
                stack.add(if (store.signedIn) "main" else "onboard")
            },
        )
        route == "help" -> HelpScreen(onBack = { pop() }, onOpenPage = { push("page:$it") })
        route.startsWith("page:") -> PolicyPageScreen(page = route.removePrefix("page:"), onBack = { pop() })
    }
    }

    NotEnoughCoinsModal(
        visible = needCoins,
        balance = store.coins,
        needed = AppStore.CallCost,
        forCall = true,
        onBuyCoins = {
            needCoins = false
            push("coins")
        },
        onFreeTry = {
            val pending = pendingCall
            needCoins = false
            if (pending != null) {
                openCall(pending.id, pending.video, pending.incoming, free = true)
            }
        },
        onDismiss = { needCoins = false },
    )
    RateAppModal(
        visible = showRate && top == "main",
        onLater = {
            RatePrompt.markShown(context)
            showRate = false
        },
        onRate = {
            RatePrompt.markRated(context)
            showRate = false
            RatePrompt.openStore(context)
        },
    )
}

private data class PendingCall(
    val id: String,
    val video: Boolean,
    val incoming: Boolean,
)
