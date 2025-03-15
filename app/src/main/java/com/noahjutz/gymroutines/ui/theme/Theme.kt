package com.noahjutz.gymroutines.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val WhiteColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryDark,
    secondary = Secondary,
    secondaryVariant = SecondaryDark,
    onPrimary = Color.White,
    onSecondary = Color.Black
)

val BlackColorPalette = darkColors(
    primary = PrimaryDesaturated,
    primaryVariant = PrimaryDark,
    secondary = Secondary,
    secondaryVariant = Secondary,
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

@Composable
fun GymRoutinesTheme(
    isDark: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDark) darkColorScheme() else lightColorScheme(),
        content = content
    )
}
