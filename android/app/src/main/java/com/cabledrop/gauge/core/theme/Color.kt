package com.cabledrop.gauge.core.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class HudColors(
    val bgDeep: Color,
    val bgBase: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val border: Color,
    val gridLine: Color,
    val primary: Color,
    val primaryDim: Color,
    val accent: Color,
    val accentDim: Color,
    val pass: Color,
    val fail: Color,
    val copperStart: Color,
    val copperEnd: Color,
    val aluminiumStart: Color,
    val aluminiumEnd: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color
)

val DarkHudColors = HudColors(
    bgDeep = Color(0xFF0B141B),
    bgBase = Color(0xFF101C24),
    surface = Color(0xFF16262F),
    surfaceAlt = Color(0xFF1F3340),
    border = Color(0xFF2B4455),
    gridLine = Color(0xFF1B3140),
    primary = Color(0xFF29B6F6),
    primaryDim = Color(0xFF0D7FB8),
    accent = Color(0xFFFFB300),
    accentDim = Color(0xFFD98E00),
    pass = Color(0xFF3FD98B),
    fail = Color(0xFFFF5A5A),
    copperStart = Color(0xFFB87333),
    copperEnd = Color(0xFFE59C5B),
    aluminiumStart = Color(0xFF9AA7AE),
    aluminiumEnd = Color(0xFFD6DEE2),
    textPrimary = Color(0xFFE6F1F7),
    textSecondary = Color(0xFF9FB6C4),
    textTertiary = Color(0xFF6B8495)
)

val LightHudColors = HudColors(
    bgDeep = Color(0xFFE7EFF5),
    bgBase = Color(0xFFF2F7FA),
    surface = Color(0xFFFFFFFF),
    surfaceAlt = Color(0xFFE3EDF3),
    border = Color(0xFFC3D4DE),
    gridLine = Color(0xFFDCE8EF),
    primary = Color(0xFF0277BD),
    primaryDim = Color(0xFF01579B),
    accent = Color(0xFFE08700),
    accentDim = Color(0xFFB26A00),
    pass = Color(0xFF1F9D5B),
    fail = Color(0xFFD23B3B),
    copperStart = Color(0xFFA2622B),
    copperEnd = Color(0xFFD08A4A),
    aluminiumStart = Color(0xFF7D8A91),
    aluminiumEnd = Color(0xFFBAC6CC),
    textPrimary = Color(0xFF0E1C24),
    textSecondary = Color(0xFF44606F),
    textTertiary = Color(0xFF6B8495)
)

val LocalHudColors = staticCompositionLocalOf { DarkHudColors }
