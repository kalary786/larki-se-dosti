package com.kalary.larkisedosti.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kalary.larkisedosti.data.LegalPage
import com.kalary.larkisedosti.ui.components.ScreenHeader
import com.kalary.larkisedosti.ui.theme.AppType
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.Space
import com.kalary.larkisedosti.ui.theme.screenInsets

@Composable
fun LegalScreen(pageId: String, onBack: () -> Unit) {
    val colors = LarkiTheme.colors
    val page = LegalPage.from(pageId)
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .screenInsets(),
    ) {
        ScreenHeader(page.title, onBack)
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(Space.s16),
        ) {
            page.body.split("\n\n").forEach { paragraph ->
                Text(
                    paragraph,
                    style = AppType.body,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(bottom = Space.s16),
                )
            }
        }
    }
}
