package com.kalary.larkisedosti

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kalary.larkisedosti.data.AppViewModel
import com.kalary.larkisedosti.data.LocalAppState
import com.kalary.larkisedosti.navigation.AppNav
import com.kalary.larkisedosti.ui.theme.LarkiTheme
import com.kalary.larkisedosti.ui.theme.MaxContentWidth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { LarkiRoot() }
    }
}

@Composable
private fun LarkiRoot() {
    val viewModel: AppViewModel = viewModel()
    val app = viewModel.app
    val view = LocalView.current
    val dark = app.darkTheme
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !dark
                isAppearanceLightNavigationBars = !dark
            }
        }
    }
    CompositionLocalProvider(LocalAppState provides app) {
        LarkiTheme(dark = dark) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(LarkiTheme.colors.background),
                contentAlignment = Alignment.TopCenter,
            ) {
                Box(
                    Modifier
                        .widthIn(max = MaxContentWidth)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .semantics { testTagsAsResourceId = true },
                ) {
                    AppNav()
                }
            }
        }
    }
}
