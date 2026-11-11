package com.cabledrop.gauge.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

private val DarkScheme = darkColorScheme(
    primary = DarkHudColors.primary,
    onPrimary = DarkHudColors.bgDeep,
    primaryContainer = DarkHudColors.primaryDim,
    onPrimaryContainer = DarkHudColors.textPrimary,
    secondary = DarkHudColors.accent,
    onSecondary = DarkHudColors.bgDeep,
    tertiary = DarkHudColors.pass,
    onTertiary = DarkHudColors.bgDeep,
    background = DarkHudColors.bgBase,
    onBackground = DarkHudColors.textPrimary,
    surface = DarkHudColors.surface,
    onSurface = DarkHudColors.textPrimary,
    surfaceVariant = DarkHudColors.surfaceAlt,
    onSurfaceVariant = DarkHudColors.textSecondary,
    outline = DarkHudColors.border,
    error = DarkHudColors.fail,
    onError = DarkHudColors.bgDeep
)

private val LightScheme = lightColorScheme(
    primary = LightHudColors.primary,
    onPrimary = LightHudColors.surface,
    primaryContainer = LightHudColors.primaryDim,
    onPrimaryContainer = LightHudColors.surface,
    secondary = LightHudColors.accent,
    onSecondary = LightHudColors.surface,
    tertiary = LightHudColors.pass,
    onTertiary = LightHudColors.surface,
    background = LightHudColors.bgBase,
    onBackground = LightHudColors.textPrimary,
    surface = LightHudColors.surface,
    onSurface = LightHudColors.textPrimary,
    surfaceVariant = LightHudColors.surfaceAlt,
    onSurfaceVariant = LightHudColors.textSecondary,
    outline = LightHudColors.border,
    error = LightHudColors.fail,
    onError = LightHudColors.surface
)

@Composable
fun AppTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalHudColors provides if (darkTheme) DarkHudColors else LightHudColors
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkScheme else LightScheme,
            typography = AppTypography,
            content = content
        )
    }
}

object AppTheme {
    val colors: HudColors
        @Composable
        @ReadOnlyComposable
        get() = LocalHudColors.current
}
