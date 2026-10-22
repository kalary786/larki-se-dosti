package com.larkisedosti.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.larkisedosti.app.model.AppStore
import com.larkisedosti.app.model.Notice
import com.larkisedosti.app.ui.components.EmptyState
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun NotificationsScreen(
    store: AppStore,
    onBack: () -> Unit,
    onOpen: (Notice) -> Unit,
) {
    val colors = LoungeTheme.colors
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = LoungeSpace.s12, vertical = LoungeSpace.s8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconCircleButton(AppIcons.ChevronLeft, onBack, contentDescription = "Back", size = 40.dp)
            Spacer(Modifier.size(LoungeSpace.s12))
            Text(text = "Notifications", color = colors.textPrimary, style = LoungeType.h2)
        }
        if (store.notices.isEmpty()) {
            EmptyState(
                title = "You're caught up",
                message = "Friend requests, messages, and gifts will show up here.",
                icon = AppIcons.Bell,
            )
        } else {
            LazyColumn {
                items(store.notices, key = { it.id }) { notice ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                notice.read = true
                                onOpen(notice)
                            }
                            .padding(horizontal = LoungeSpace.s20, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (notice.read) colors.border else colors.primary),
                        )
                        Spacer(Modifier.size(LoungeSpace.s12))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = notice.title,
                                color = colors.textPrimary,
                                style = LoungeType.title.copy(
                                    fontWeight = if (notice.read) FontWeight.SemiBold else FontWeight.Bold,
                                ),
                            )
                            Text(text = notice.body, color = colors.textSecondary, style = LoungeType.body)
                        }
                        Text(text = notice.time, color = colors.textSecondary, style = LoungeType.caption)
                    }
                }
            }
        }
    }
}
