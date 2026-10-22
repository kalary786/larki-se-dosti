package com.larkisedosti.app.ui.screens

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.larkisedosti.app.ui.components.IconCircleButton
import com.larkisedosti.app.ui.icons.AppIcons
import com.larkisedosti.app.ui.theme.LoungeSpace
import com.larkisedosti.app.ui.theme.LoungeTheme
import com.larkisedosti.app.ui.theme.LoungeType

@Composable
fun PolicyPageScreen(
    page: String,
    onBack: () -> Unit,
) {
    val colors = LoungeTheme.colors
    val file = when (page) {
        "privacy" -> "privacy.html"
        "support" -> "support.html"
        "delete" -> "delete-account.html"
        "safety" -> "child-safety.html"
        else -> "index.html"
    }
    val title = when (page) {
        "privacy" -> "Privacy policy"
        "support" -> "Support"
        "delete" -> "Account deletion"
        "safety" -> "Child safety"
        else -> "Larki Se Dosti"
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = LoungeSpace.s20, vertical = LoungeSpace.s12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconCircleButton(AppIcons.ChevronLeft, onBack, contentDescription = "Back", size = 40.dp)
            Spacer(Modifier.size(LoungeSpace.s12))
            Text(text = title, color = colors.textPrimary, style = LoungeType.h2)
        }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = false
                    settings.allowFileAccess = true
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                            val url = request.url ?: return false
                            val scheme = url.scheme.orEmpty()
                            if (scheme == "mailto" || scheme == "http" || scheme == "https") {
                                context.startActivity(Intent(Intent.ACTION_VIEW, url))
                                return true
                            }
                            return false
                        }
                    }
                    loadUrl("file:///android_asset/web/$file")
                }
            },
            update = { webView ->
                if (webView.tag != file) {
                    webView.tag = file
                    webView.loadUrl("file:///android_asset/web/$file")
                }
            },
        )
    }
}
