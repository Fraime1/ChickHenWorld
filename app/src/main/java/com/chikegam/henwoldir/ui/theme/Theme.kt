package com.chikegam.henwoldir.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ===== LIGHT THEME COLOR SCHEME =====
private val LightColorScheme = lightColorScheme(
    // Primary colors
    primary = YellowPrimary,
    onPrimary = Black,
    primaryContainer = YellowSecondary,
    onPrimaryContainer = Black,
    
    // Secondary colors
    secondary = GreenAccent,
    onSecondary = White,
    secondaryContainer = GreenAccentVariant,
    onSecondaryContainer = Black,
    
    // Tertiary colors
    tertiary = BlueAccent,
    onTertiary = White,
    tertiaryContainer = BlueAccentVariant,
    onTertiaryContainer = Black,
    
    // Background colors
    background = CreamBackground,
    onBackground = Black,
    surface = CreamSurface,
    onSurface = Black,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = DarkGray,
    
    // Container colors
    surfaceContainer = CreamSurface,
    surfaceContainerHigh = CreamSurfaceVariant,
    surfaceContainerHighest = White,
    
    // Outline colors
    outline = MediumGray,
    outlineVariant = LightGray,
    
    // Error colors
    error = RedAccent,
    onError = White,
    errorContainer = RedAccentVariant,
    onErrorContainer = Black,
    
    // Inverse colors
    inverseSurface = DarkGray,
    inverseOnSurface = White,
    inversePrimary = YellowPrimaryVariant,
    
    // Scrim
    scrim = Black.copy(alpha = 0.5f)
)

// ===== DARK THEME COLOR SCHEME =====
private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = YellowPrimaryDark,
    onPrimary = Black,
    primaryContainer = YellowPrimaryVariantDark,
    onPrimaryContainer = Black,
    
    // Secondary colors
    secondary = GreenAccentDark,
    onSecondary = Black,
    secondaryContainer = GreenAccentVariantDark,
    onSecondaryContainer = Black,
    
    // Tertiary colors
    tertiary = BlueAccentDark,
    onTertiary = Black,
    tertiaryContainer = BlueAccentVariantDark,
    onTertiaryContainer = Black,
    
    // Background colors
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    
    // Container colors
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceVariant,
    surfaceContainerHighest = DarkSurface,
    
    // Outline colors
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    
    // Error colors
    error = RedAccentDark,
    onError = Black,
    errorContainer = RedAccentVariantDark,
    onErrorContainer = Black,
    
    // Inverse colors
    inverseSurface = DarkOnSurface,
    inverseOnSurface = DarkBackground,
    inversePrimary = YellowPrimary,
    
    // Scrim
    scrim = Black.copy(alpha = 0.7f)
)

@Composable
fun ChickHenWorldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    // Set status bar colors for better integration
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}