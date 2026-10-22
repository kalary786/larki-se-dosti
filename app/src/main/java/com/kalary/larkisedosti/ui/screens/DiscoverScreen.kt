package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.kalary.larkisedosti.data.DiscoverFilter
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.data.Person
import androidx.compose.foundation.layout.Column
import com.kalary.larkisedosti.ui.components.AdSlot
import com.kalary.larkisedosti.ui.components.CountryPickerSheet
import com.kalary.larkisedosti.ui.components.FilterChip
import com.kalary.larkisedosti.ui.components.ProfileCard
import com.kalary.larkisedosti.ui.components.SkeletonCard
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.tag
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private sealed interface FeedItem {
    val key: String
    data class Profile(val person: Person) : FeedItem { override val key = person.id }
    data class NativeAd(val after: Int) : FeedItem { override val key = "ad-$after" }
}

@Composable
fun DiscoverScreen(
    onOpenProfile: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onCall: (String, Boolean) -> Unit,
) {
    val app = LocalAppState.current
    val scope = rememberCoroutineScope()
    var showCountries by remember { mutableStateOf(false) }
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {
        if (app.discoverLoading) {
            delay(650)
            app.discoverLoading = false
        }
    }
    val people = app.visiblePeople()
    val feed = buildList {
        people.forEachIndexed { index, person ->
            add(FeedItem.Profile(person))
            if (!app.isVip && (index + 1) % 6 == 0) add(FeedItem.NativeAd(index))
        }
    }
    LaunchedEffect(gridState, feed.size) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1 }
            .collect { last ->
                if (last >= feed.size - 3 && !app.loadingMore && app.discoverExtras < 6 && !app.discoverLoading) {
                    app.loadingMore = true
                    delay(450)
                    app.appendDiscoverPage()
                    app.loadingMore = false
                }
            }
    }
    Column(Modifier.fillMaxSize()) {
    ColumnChips(
        countryLabel = app.discoverCountry?.name ?: "Country",
        filter = app.discoverFilter,
        onAll = { app.discoverFilter = DiscoverFilter.All },
        onGirls = { app.discoverFilter = DiscoverFilter.Girls },
        onBoys = { app.discoverFilter = DiscoverFilter.Boys },
        onCountry = { showCountries = true },
    )
    PullToRefreshBox(
        isRefreshing = app.discoverRefreshing,
        onRefresh = {
            scope.launch {
                if (app.discoverRefreshing) return@launch
                app.discoverRefreshing = true
                delay(700)
                app.reshuffleDiscover()
                app.discoverRefreshing = false
            }
        },
        modifier = Modifier.weight(1f).fillMaxSize(),
    ) {
        if (app.discoverLoading) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(Space.s16),
                horizontalArrangement = Arrangement.spacedBy(Space.s12),
                verticalArrangement = Arrangement.spacedBy(Space.s12),
            ) {
                items(6) { SkeletonCard() }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier.tag("screen_discover"),
                contentPadding = PaddingValues(Space.s16),
                horizontalArrangement = Arrangement.spacedBy(Space.s12),
                verticalArrangement = Arrangement.spacedBy(Space.s12),
            ) {
                items(feed, key = { it.key }) { item ->
                    when (item) {
                        is FeedItem.Profile -> {
                            val person = item.person
                            val first = feed.filterIsInstance<FeedItem.Profile>().firstOrNull()?.person?.id == person.id
                            ProfileCard(
                                person = person,
                                favorite = app.isFavorite(person.id),
                                friend = app.isFriend(person.id),
                                onOpen = { onOpenProfile(app.baseId(person.id)) },
                                onFavorite = { app.toggleFavorite(person.id) },
                                onMessage = { onOpenChat(app.baseId(person.id)) },
                                onVideo = { onCall(person.id, true) },
                                onAddFriend = { app.addFriend(person.id) },
                                modifier = Modifier
                                    .aspectRatio(0.72f)
                                    .then(if (first) Modifier.tag("profile_card_first") else Modifier),
                            )
                        }
                        is FeedItem.NativeAd -> Box(Modifier.aspectRatio(0.72f)) { AdSlot(native = true, modifier = Modifier.fillMaxSize()) }
                    }
                }
                if (app.loadingMore) {
                    items(2) { SkeletonCard() }
                }
            }
        }
    }
    }
    if (showCountries) {
        CountryPickerSheet(
            onDismiss = { showCountries = false },
            onPick = {
                app.discoverCountry = it
                app.discoverFilter = DiscoverFilter.Country
            },
        )
    }
}

@Composable
private fun ColumnChips(
    countryLabel: String,
    filter: DiscoverFilter,
    onAll: () -> Unit,
    onGirls: () -> Unit,
    onBoys: () -> Unit,
    onCountry: () -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        Modifier.padding(horizontal = Space.s16).padding(bottom = Space.s8),
        horizontalArrangement = Arrangement.spacedBy(Space.s8),
    ) {
        FilterChip("All", filter == DiscoverFilter.All, onAll)
        FilterChip("Girls", filter == DiscoverFilter.Girls, onGirls)
        FilterChip("Boys", filter == DiscoverFilter.Boys, onBoys)
        FilterChip(countryLabel, filter == DiscoverFilter.Country, onCountry)
    }
}
