package com.example.yumeat_25.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val button_green_primary = Color(0xFF1F5F5B)
private val text_blue_onPrimary = Color(0xFF0694F4)
private val primaryContainer_white = Color(0x63999999)
private val onPrimaryContainer_black = Color(0xFF000000)

private val LightColors = lightColorScheme(
    primary = button_green_primary,
    onPrimary = text_blue_onPrimary,
    primaryContainer = primaryContainer_white,
    onPrimaryContainer = onPrimaryContainer_black
)

@Composable
fun YUMeatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColors

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
        content = content
    )
}