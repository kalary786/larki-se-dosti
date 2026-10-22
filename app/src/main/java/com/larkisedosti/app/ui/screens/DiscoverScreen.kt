package com.larkisedosti.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.Countries
import com.larkisedosti.app.model.Country
import com.larkisedosti.app.model.Gender
import com.larkisedosti.app.model.Person
import com.larkisedosti.app.ui.components.AdSlot
import com.larkisedosti.app.ui.components.BottomSheet
import com.larkisedosti.app.ui.components.DiscoverSkeletonCard
import com.larkisedosti.app.ui.components.FilterChip
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.components.LoungeTextField
import com.larkisedosti.app.ui.components.rememberSkeletonPulse
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeShape
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType
import com.larkisedosti.app.ui.shell.LocalShellInset
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private sealed interface FeedItem {
    data class Profile(val person: Person) : FeedItem
    data class Ad(val key: String) : FeedItem
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    store: AppStore,
    onOpenProfile: (String) -> Unit,
    onMessage: (String) -> Unit,
    onVideo: (String) -> Unit,
) {
    val colors = LoungeTheme.colors
    var filter by remember { mutableStateOf("All") }
    var country by remember { mutableStateOf<Country?>(null) }
    var countryOpen by remember { mutableStateOf(false) }
    var countryQuery by remember { mutableStateOf("") }
    var ready by remember { mutableStateOf(false) }
    var refreshing by remember { mutableStateOf(false) }
    var paging by remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()

    val people = store.directory.filter { person ->
        person.id !in store.blockedIds && when (filter) {
            "Girls" -> person.gender == Gender.Female
            "Boys" -> person.gender == Gender.Male
            "Country" -> country == null || person.countryName == country?.name
            else -> true
        }
    }
    val feed = buildFeed(people)

    LaunchedEffect(Unit) {
        delay(700)
        ready = true
    }
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(700)
            refreshing = false
            ready = true
        }
    }
    LaunchedEffect(ready, feed.size) {
        androidx.compose.runtime.snapshotFlow {
            gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
        }
            .map { it }
            .distinctUntilChanged()
            .collect { last ->
                if (ready && !paging && last >= feed.size - 4 && store.directory.size < 48) {
                    paging = true
                }
            }
    }
    LaunchedEffect(paging) {
        if (!paging) return@LaunchedEffect
        delay(400)
        store.appendPage()
        paging = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = LoungeSpace.s16, vertical = LoungeSpace.s12),
            horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s8),
        ) {
            listOf("All", "Girls", "Boys").forEach { label ->
                FilterChip(
                    label = label,
                    selected = filter == label,
                    onClick = { filter = label },
                )
            }
            FilterChip(
                label = country?.name ?: "Country",
                selected = filter == "Country",
                onClick = {
                    filter = "Country"
                    countryOpen = true
                },
            )
        }
        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = {
                refreshing = true
                ready = false
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            if (!ready) {
                val pulse = rememberSkeletonPulse()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = LoungeSpace.s16,
                        end = LoungeSpace.s16,
                        top = LoungeSpace.s4,
                        bottom = LocalShellInset.current,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s12),
                    verticalArrangement = Arrangement.spacedBy(LoungeSpace.s12),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(6) {
                        DiscoverSkeletonCard(pulse = pulse)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    contentPadding = PaddingValues(
                        start = LoungeSpace.s16,
                        end = LoungeSpace.s16,
                        top = LoungeSpace.s4,
                        bottom = LocalShellInset.current,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(LoungeSpace.s12),
                    verticalArrangement = Arrangement.spacedBy(LoungeSpace.s12),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(feed, key = { item ->
                        when (item) {
                            is FeedItem.Profile -> item.person.id
                            is FeedItem.Ad -> item.key
                        }
                    }) { item ->
                        when (item) {
                            is FeedItem.Ad -> Box(Modifier.aspectRatio(0.68f)) {
                                AdSlot(matchParent = true, label = "Ad")
                            }
                            is FeedItem.Profile -> DiscoverCard(
                                person = item.person,
                                saved = item.person.id in store.favorites,
                                friend = item.person.id in store.friendIds,
                                onOpen = { onOpenProfile(item.person.id) },
                                onSave = { store.toggleFavorite(item.person.id) },
                                onMessage = { onMessage(item.person.id) },
                                onVideo = { onVideo(item.person.id) },
                                onAddFriend = { store.addFriend(item.person.id) },
                            )
                        }
                    }
                }
            }
        }
    }

    BottomSheet(visible = countryOpen, onDismiss = { countryOpen = false }) {
        Text(text = "Country", color = colors.textPrimary, style = LoungeType.h2)
        Spacer(Modifier.height(LoungeSpace.s12))
        LoungeTextField(
            value = countryQuery,
            onValueChange = { countryQuery = it },
            placeholder = "Search countries",
            leadingIcon = AppIcons.Search,
        )
        Spacer(Modifier.height(LoungeSpace.s12))
        LazyColumn(modifier = Modifier.height(360.dp)) {
            items(
                Countries.filter { countryQuery.isBlank() || it.name.contains(countryQuery, true) },
                key = { it.name },
            ) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            country = item
                            filter = "Country"
                            countryOpen = false
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = item.flag, style = LoungeType.title)
                    Spacer(Modifier.size(LoungeSpace.s12))
                    Text(text = item.name, color = colors.textPrimary, style = LoungeType.body)
                }
            }
        }
    }
}

private fun buildFeed(people: List<Person>): List<FeedItem> {
    val feed = mutableListOf<FeedItem>()
    people.forEachIndexed { index, person ->
        feed += FeedItem.Profile(person)
        if ((index + 1) % 6 == 0) feed += FeedItem.Ad("ad-${person.id}")
    }
    return feed
}

@Composable
private fun DiscoverCard(
    person: Person,
    saved: Boolean,
    friend: Boolean,
    onOpen: () -> Unit,
    onSave: () -> Unit,
    onMessage: () -> Unit,
    onVideo: () -> Unit,
    onAddFriend: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .aspectRatio(0.68f)
            .clip(LoungeShape.card)
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = Color.White.copy(alpha = 0.12f)),
                onClick = onOpen,
            ),
    ) {
        Box(modifier = Modifier.fillMaxSize().background(person.color)) {
            Text(
                text = person.name.take(1),
                color = Color.White,
                style = LoungeType.h1,
                modifier = Modifier.align(Alignment.Center),
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
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.62f)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xFF14112B).copy(alpha = 0.94f)),
                    ),
                ),
        )
        if (person.online) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(LoungeShape.chip)
                    .background(Color(0xFF14112B).copy(alpha = 0.72f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(LoungeTheme.colors.online),
                )
                Spacer(Modifier.size(6.dp))
                Text(text = "Online", color = Color.White, style = LoungeType.caption)
            }
        }
        IconCircleButton(
            icon = AppIcons.Heart,
            contentDescription = if (saved) "Saved" else "Save",
            onClick = onSave,
            size = 34.dp,
            tint = if (saved) LoungeTheme.colors.primary else Color.White,
            containerColor = Color(0xFF14112B).copy(alpha = 0.55f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${person.name}, ${person.age}",
                    color = Color.White,
                    style = LoungeType.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                if (person.verified) {
                    Spacer(Modifier.size(4.dp))
                    VerifiedMark()
                }
            }
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = AppIcons.MapPin,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(12.dp),
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = "${person.city}, ${person.countryName}",
                    color = Color.White.copy(alpha = 0.86f),
                    style = LoungeType.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconCircleButton(
                    icon = AppIcons.Message,
                    contentDescription = "Message",
                    onClick = onMessage,
                    size = 32.dp,
                    tint = Color.White,
                    containerColor = Color(0xFF2A2550),
                )
                IconCircleButton(
                    icon = AppIcons.Video,
                    contentDescription = "Video call",
                    onClick = onVideo,
                    size = 32.dp,
                    tint = LoungeTheme.colors.primary,
                    containerColor = Color(0xFF2A2550),
                )
                IconCircleButton(
                    icon = if (friend) AppIcons.Check else AppIcons.UserPlus,
                    contentDescription = if (friend) "Friends" else "Add friend",
                    onClick = onAddFriend,
                    size = 32.dp,
                    tint = Color.White,
                    containerColor = Color(0xFF2A2550),
                )
            }
        }
    }
}

@Composable
private fun VerifiedMark() {
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(LoungeTheme.colors.online),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AppIcons.Check,
            contentDescription = "Verified",
            tint = Color.White,
            modifier = Modifier.size(10.dp),
        )
    }
}
