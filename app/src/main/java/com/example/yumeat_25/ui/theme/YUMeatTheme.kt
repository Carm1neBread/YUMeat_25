package com.example.yumeat_25.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Colori caldi e accoglienti per YUMeat
val button_green_primary = Color(0xFF1F5F5B)
private val text_blue_onPrimary = Color(0xFF0694F4)
private val primaryContainer_white = Color(0x63999999)
private val onPrimaryContainer_black = Color(0xFF000000)

private val md_theme_light_secondary = Color(0xFF6F5B40)
private val md_theme_light_onSecondary = Color(0xFFFFFFFF)
private val md_theme_light_secondaryContainer = Color(0xFFF5E6D3)
private val md_theme_light_onSecondaryContainer = Color(0xFF2A1F0C)

private val md_theme_light_tertiary = Color(0xFF51643F)
private val md_theme_light_onTertiary = Color(0xFFFFFFFF)
private val md_theme_light_tertiaryContainer = Color(0xFFD3EABA)
private val md_theme_light_onTertiaryContainer = Color(0xFF0F2000)

private val md_theme_light_error = Color(0xFFBA1A1A)
private val md_theme_light_errorContainer = Color(0xFFFFEDEA)
private val md_theme_light_onError = Color(0xFFFFFFFF)
private val md_theme_light_onErrorContainer = Color(0xFF410002)

private val md_theme_light_background = Color(0xFFFFFBF7)
private val md_theme_light_onBackground = Color(0xFF1F1B16)
private val md_theme_light_surface = Color(0xFFFFFBF7)
private val md_theme_light_onSurface = Color(0xFF1F1B16)

private val LightColors = lightColorScheme(
    primary = button_green_primary,
    onPrimary = text_blue_onPrimary,
    primaryContainer = primaryContainer_white,
    onPrimaryContainer = onPrimaryContainer_black,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
)

@Composable
fun YUMeatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> LightColors // Usa sempre il tema chiaro per un feel più caldo
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}