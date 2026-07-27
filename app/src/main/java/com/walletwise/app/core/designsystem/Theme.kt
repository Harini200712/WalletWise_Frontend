package com.walletwise.app.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = WalletPrimary,
    onPrimary = WalletSurface,
    primaryContainer = WalletPrimaryLight,
    onPrimaryContainer = WalletPrimaryDark,
    secondary = WalletAccentOrange,
    onSecondary = WalletTextPrimary,
    tertiary = WalletAccentCoral,
    background = WalletBackground,
    onBackground = WalletTextPrimary,
    surface = WalletSurface,
    onSurface = WalletTextPrimary,
    surfaceVariant = WalletPrimaryLight,
    onSurfaceVariant = WalletTextSecondary,
    outline = WalletDivider,
    error = WalletError
)

private val DarkColorScheme = darkColorScheme(
    primary = WalletPrimary,
    onPrimary = WalletSurface,
    primaryContainer = WalletPrimaryDark,
    onPrimaryContainer = WalletPrimaryLight,
    secondary = WalletAccentOrange,
    onSecondary = WalletTextPrimary,
    tertiary = WalletAccentCoral,
    background = WalletDarkBackground,
    onBackground = WalletDarkTextPrimary,
    surface = WalletDarkSurface,
    onSurface = WalletDarkTextPrimary,
    surfaceVariant = WalletDarkSurface,
    onSurfaceVariant = WalletDarkTextSecondary,
    outline = WalletDarkDivider,
    error = WalletError
)

@Composable
fun WalletWiseTheme(
    themeMode: String = "Light",
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        "Dark" -> true
        "Light" -> false
        else -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = WalletTypography,
        shapes = WalletShapes,
        content = content
    )
}
