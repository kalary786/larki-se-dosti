package com.kalary.larkisedosti.ui.theme

import androidx.compose.ui.graphics.Color

data class LarkiPalette(
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val online: Color,
    val danger: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val shadow: Color,
    val scrim: Color,
    val onPhoto: Color,
    val overlay: Color,
    val ink: Color,
    val clear: Color,
    val portraits: List<Color>,
    val skins: List<Color>,
    val hair: List<Color>,
    val shirts: List<Color>,
)

private val PortraitBackgrounds = listOf(
    Color(0xFF3A2E6B),
    Color(0xFF5C3B4A),
    Color(0xFF2C4A5E),
    Color(0xFF4A3B2A),
    Color(0xFF2E4A3D),
    Color(0xFF4A3058),
    Color(0xFF3E3A6B),
    Color(0xFF5A4032),
    Color(0xFF243E56),
    Color(0xFF4E3450),
    Color(0xFF2F4F45),
    Color(0xFF53363A),
)

private val SkinTones = listOf(
    Color(0xFFF0C9A0),
    Color(0xFFE0B08C),
    Color(0xFFC68642),
    Color(0xFF8D5524),
    Color(0xFFFFD7B5),
    Color(0xFF6B3E26),
)

private val HairTones = listOf(
    Color(0xFF1A1228),
    Color(0xFF3B2416),
    Color(0xFF2A2118),
    Color(0xFF4A3728),
    Color(0xFF151515),
)

private val ShirtTones = listOf(
    Color(0xFF2A2550),
    Color(0xFF3D3358),
    Color(0xFF243044),
    Color(0xFF4A3040),
    Color(0xFF1F3A36),
)

private val Ink = Color(0xFF14112B)
private val Clear = Color.Transparent
private val OnPrimary = Color(0xFFFFFFFF)
private val OnPhoto = Color(0xFFFFFFFF)
private val Scrim = Color(0xFF14112B)
private val Overlay = Color(0x8C14112B)
private val Primary = Color(0xFFFF7A59)
private val Secondary = Color(0xFFFFC75F)
private val Online = Color(0xFF3DDC97)
private val Danger = Color(0xFFFF4D6D)

internal val DarkPalette = LarkiPalette(
    background = Color(0xFF14112B),
    surface = Color(0xFF1F1B3A),
    surfaceElevated = Color(0xFF2A2550),
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = Ink,
    online = Online,
    danger = Danger,
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFB7B3D1),
    border = Color.White.copy(alpha = 0.08f),
    shadow = Color(0x66000000),
    scrim = Scrim,
    onPhoto = OnPhoto,
    overlay = Overlay,
    ink = Ink,
    clear = Clear,
    portraits = PortraitBackgrounds,
    skins = SkinTones,
    hair = HairTones,
    shirts = ShirtTones,
)

internal val LightPalette = LarkiPalette(
    background = Color(0xFFF7F5FF),
    surface = Color(0xFFFFFFFF),
    surfaceElevated = Color(0xFFFFFFFF),
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = Ink,
    online = Online,
    danger = Danger,
    textPrimary = Color(0xFF14112B),
    textSecondary = Color(0xFF5C5878),
    border = Color(0xFF14112B).copy(alpha = 0.08f),
    shadow = Color(0x2414112B),
    scrim = Scrim,
    onPhoto = OnPhoto,
    overlay = Overlay,
    ink = Ink,
    clear = Clear,
    portraits = PortraitBackgrounds,
    skins = SkinTones,
    hair = HairTones,
    shirts = ShirtTones,
)
