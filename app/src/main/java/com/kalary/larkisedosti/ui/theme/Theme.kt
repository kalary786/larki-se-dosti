package com.kalary.larkisedosti.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalPalette = staticCompositionLocalOf { DarkPalette }

object LarkiTheme {
    val colors: LarkiPalette
        @Composable get() = LocalPalette.current
}

@Composable
fun LarkiTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val palette = if (dark) DarkPalette else LightPalette
    val scheme = if (dark) {
        darkColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            secondary = palette.secondary,
            onSecondary = palette.onSecondary,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.surface,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.surfaceElevated,
            onSurfaceVariant = palette.textSecondary,
            error = palette.danger,
            onError = palette.onPrimary,
            outline = palette.border,
        )
    } else {
        lightColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            secondary = palette.secondary,
            onSecondary = palette.onSecondary,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.surface,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.surfaceElevated,
            onSurfaceVariant = palette.textSecondary,
            error = palette.danger,
            onError = palette.onPrimary,
            outline = palette.border,
        )
    }
    CompositionLocalProvider(LocalPalette provides palette) {
        MaterialTheme(
            colorScheme = scheme,
            typography = LarkiTypography,
            shapes = LarkiShapes,
            content = content,
        )
    }
}
