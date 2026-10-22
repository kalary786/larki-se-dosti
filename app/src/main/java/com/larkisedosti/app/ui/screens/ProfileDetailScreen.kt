package com.larkisedosti.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.GiftKind
import com.larkisedosti.app.model.Person
import com.larkisedosti.app.ui.components.BlockUserDialog
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.LoungeMenu
import com.larkisedosti.app.ui.components.MenuEntry
import com.larkisedosti.app.ui.components.NotEnoughCoinsModal
import com.larkisedosti.app.ui.components.ReportUserDialog
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun ProfileDetailScreen(
    store: AppStore,
    personId: String,
    onBack: () -> Unit,
    onMessage: () -> Unit,
    onVoice: () -> Unit,
    onVideo: () -> Unit,
    onCoins: () -> Unit,
) {
    val person = store.person(personId)
    LaunchedEffect(person) {
        if (person == null) onBack()
    }
    if (person == null) return
    val colors = LoungeTheme.colors
    var menu by rememberSaveable { mutableStateOf(false) }
    var report by rememberSaveable { mutableStateOf(false) }
    var block by rememberSaveable { mutableStateOf(false) }
    var giftOpen by rememberSaveable { mutableStateOf(false) }
    var pendingGift by remember { mutableStateOf<GiftKind?>(null) }
    var needCoins by rememberSaveable { mutableStateOf(false) }
    var burst by rememberSaveable { mutableStateOf<String?>(null) }
    val panels = person.panels.coerceIn(1, 3)
    val pager = rememberPagerState { panels }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
            ) {
                HorizontalPager(state = pager, modifier = Modifier.fillMaxSize()) { page ->
                    val shade = 1f - page * 0.08f
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color(
                                    red = person.color.red * shade,
                                    green = person.color.green * shade,
                                    blue = person.color.blue * shade,
                                ),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = person.name.take(1),
                            color = Color.White,
                            style = LoungeType.h1,
                        )
                        person.photoUrl?.let { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = person.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(horizontal = LoungeSpace.s12, vertical = LoungeSpace.s8)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconCircleButton(
                        icon = AppIcons.ChevronLeft,
                        contentDescription = "Back",
                        onClick = onBack,
                        size = 40.dp,
                    )
                    Spacer(Modifier.weight(1f))
                    Box {
                        IconCircleButton(
                            icon = AppIcons.MoreVertical,
                            contentDescription = "More",
                            onClick = { menu = true },
                            size = 40.dp,
                        )
                        LoungeMenu(
                            expanded = menu,
                            onDismiss = { menu = false },
                            entries = listOf(
                                MenuEntry("Report", AppIcons.Flag) { report = true },
                                MenuEntry("Block", AppIcons.Ban, danger = true) { block = true },
                            ),
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = LoungeSpace.s12),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    repeat(panels) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (pager.currentPage == index) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pager.currentPage == index) Color.White
                                    else Color.White.copy(alpha = 0.45f),
                                ),
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(LoungeSpace.s20)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${person.name}, ${person.age}",
                        color = colors.textPrimary,
                        style = LoungeType.h2,
                    )
                    if (person.verified) {
                        Spacer(Modifier.size(6.dp))
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(colors.online),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = AppIcons.Check,
                                contentDescription = "Verified",
                                tint = Color.White,
                                modifier = Modifier.size(11.dp),
                            )
                        }
                    }
                }
                Spacer(Modifier.height(LoungeSpace.s4))
                Text(
                    text = if (person.online) "Online" else "Offline",
                    color = if (person.online) colors.online else colors.textSecondary,
                    style = LoungeType.caption,
                )
                Spacer(Modifier.height(LoungeSpace.s4))
                Text(
                    text = "${person.countryFlag}  ${person.city}, ${person.countryName}",
                    color = colors.textSecondary,
                    style = LoungeType.body,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(LoungeSpace.s12))
                Text(text = person.bio, color = colors.textPrimary, style = LoungeType.body)
                Spacer(Modifier.height(LoungeSpace.s20))
                ActionRow(
                    person = person,
                    friend = person.id in store.friendIds,
                    onMessage = onMessage,
                    onVoice = onVoice,
                    onVideo = onVideo,
                    onAddFriend = { store.addFriend(person.id) },
                    onGift = { giftOpen = true },
                )
            }
        }
        burst?.let { title ->
            GiftBurst(title = title, onFinished = { burst = null })
        }
    }

    SendGiftSheet(
        visible = giftOpen,
        recipient = person.name,
        balance = store.coins,
        onClose = { giftOpen = false },
        onAddCoins = {
            giftOpen = false
            onCoins()
        },
        onSend = { gift ->
            if (store.sendGift(person.id, gift)) {
                giftOpen = false
                burst = gift.title
            } else {
                pendingGift = gift
                needCoins = true
            }
        },
    )
    NotEnoughCoinsModal(
        visible = needCoins,
        balance = store.coins,
        needed = pendingGift?.price ?: AppStore.CallCost,
        forCall = false,
        onBuyCoins = {
            needCoins = false
            giftOpen = false
            onCoins()
        },
        onFreeTry = {
            val gift = pendingGift
            needCoins = false
            if (gift != null && store.sendGift(person.id, gift, free = true)) {
                giftOpen = false
                burst = gift.title
            }
        },
        onDismiss = { needCoins = false },
    )
    ReportUserDialog(
        visible = report,
        name = person.name,
        onDismiss = { report = false },
        onSubmit = { reason, note ->
            store.fileReport(person.id, reason, note)
            report = false
        },
    )
    BlockUserDialog(
        visible = block,
        name = person.name,
        onDismiss = { block = false },
        onConfirm = {
            store.block(person.id)
            block = false
            onBack()
        },
    )
}

@Composable
private fun ActionRow(
    person: Person,
    friend: Boolean,
    onMessage: () -> Unit,
    onVoice: () -> Unit,
    onVideo: () -> Unit,
    onAddFriend: () -> Unit,
    onGift: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Action("Message", AppIcons.Message, LoungeTheme.colors.textPrimary, onMessage)
        Action("Voice", AppIcons.Phone, LoungeTheme.colors.online, onVoice)
        Action("Video", AppIcons.Video, LoungeTheme.colors.primary, onVideo)
        Action(if (friend) "Friends" else "Add", if (friend) AppIcons.Check else AppIcons.UserPlus, LoungeTheme.colors.textPrimary, onAddFriend)
        Action("Gift", AppIcons.Gift, LoungeTheme.colors.coin, onGift)
    }
}

@Composable
private fun Action(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    onClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconCircleButton(
            icon = icon,
            contentDescription = label,
            onClick = onClick,
            size = 44.dp,
            tint = tint,
        )
        Spacer(Modifier.height(6.dp))
        Text(text = label, color = LoungeTheme.colors.textSecondary, style = LoungeType.caption)
    }
}
