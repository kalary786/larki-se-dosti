package com.larkisedosti.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.larkisedosti.app.R

// Plus Jakarta Sans is bundled under the SIL Open Font License, Version 1.1.

private val Night = Color(0xFF14112B)
private val SurfaceDark = Color(0xFF1F1B3A)
private val ElevatedDark = Color(0xFF2A2550)
private val Peach = Color(0xFFFF7A59)
private val Gold = Color(0xFFFFC75F)
private val Mint = Color(0xFF3DDC97)
private val DangerRed = Color(0xFFFF4D6D)
private val Ink = Color(0xFFFFFFFF)
private val Muted = Color(0xFFB7B3D1)

private val Day = Color(0xFFF7F5FF)
private val SurfaceLight = Color(0xFFFFFFFF)
private val InkLight = Color(0xFF16132B)
private val MutedLight = Color(0xFF6E6A8A)

val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
)

object LoungeType {
    val h1 = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    )
    val h2 = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    )
    val title = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
    )
    val body = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    )
    val caption = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )
}

object LoungeSpace {
    val s4 = 4.dp
    val s8 = 8.dp
    val s12 = 12.dp
    val s16 = 16.dp
    val s20 = 20.dp
    val s24 = 24.dp
    val s32 = 32.dp
}

object LoungeShape {
    val card: Shape = RoundedCornerShape(20.dp)
    val button: Shape = RoundedCornerShape(14.dp)
    val chip: Shape = RoundedCornerShape(999.dp)
    val input: Shape = RoundedCornerShape(16.dp)
}

@Immutable
data class LoungeColors(
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val primary: Color,
    val coin: Color,
    val online: Color,
    val danger: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val outline: Color,
    val onPrimary: Color,
    val isLight: Boolean,
)

val DarkLoungeColors = LoungeColors(
    background = Night,
    surface = SurfaceDark,
    surfaceElevated = ElevatedDark,
    primary = Peach,
    coin = Gold,
    online = Mint,
    danger = DangerRed,
    textPrimary = Ink,
    textSecondary = Muted,
    border = Color.White.copy(alpha = 0.08f),
    outline = Color.White.copy(alpha = 0.16f),
    onPrimary = Color.White,
    isLight = false,
)

val LightLoungeColors = LoungeColors(
    background = Day,
    surface = SurfaceLight,
    surfaceElevated = SurfaceLight,
    primary = Peach,
    coin = Gold,
    online = Mint,
    danger = DangerRed,
    textPrimary = InkLight,
    textSecondary = MutedLight,
    border = Color(0xFF14112B).copy(alpha = 0.08f),
    outline = Color(0xFF14112B).copy(alpha = 0.14f),
    onPrimary = Color.White,
    isLight = true,
)

val LocalLoungeColors = staticCompositionLocalOf { DarkLoungeColors }

object LoungeTheme {
    val colors: LoungeColors
        @Composable
        get() = LocalLoungeColors.current
}

fun Modifier.loungeShadow(shape: Shape, light: Boolean): Modifier {
    val tint = if (light) Color(0xFF14112B) else Color.Black
    val alpha = if (light) 0.07f else 0.24f
    return this.shadow(
        elevation = 8.dp,
        shape = shape,
        clip = false,
        ambientColor = tint.copy(alpha = alpha),
        spotColor = tint.copy(alpha = alpha),
    )
}

@Composable
fun LarkiSeDostiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkLoungeColors else LightLoungeColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            window.navigationBarColor = colors.background.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = colors.isLight
            controller.isAppearanceLightNavigationBars = colors.isLight
        }
    }

    val scheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            background = colors.background,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.surfaceElevated,
            onSurfaceVariant = colors.textSecondary,
            error = colors.danger,
            onError = Color.White,
            outline = colors.border,
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            background = colors.background,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.surfaceElevated,
            onSurfaceVariant = colors.textSecondary,
            error = colors.danger,
            onError = Color.White,
            outline = colors.border,
        )
    }

    CompositionLocalProvider(LocalLoungeColors provides colors) {
        MaterialTheme(
            colorScheme = scheme,
            typography = Typography(
                headlineLarge = LoungeType.h1,
                headlineMedium = LoungeType.h2,
                titleMedium = LoungeType.title,
                bodyLarge = LoungeType.body,
                bodyMedium = LoungeType.body,
                labelMedium = LoungeType.caption,
            ),
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colors.background,
                contentColor = colors.textPrimary,
                content = content,
            )
        }
    }
}
