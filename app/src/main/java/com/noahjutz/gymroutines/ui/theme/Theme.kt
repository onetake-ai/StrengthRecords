package com.noahjutz.gymroutines.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = GoldenGrass,
    onPrimary = BleachedCedar,
    primaryContainer = GoldenGrass,
    onPrimaryContainer = BleachedCedar,
    secondary = Cabaret,
    onSecondary = Alabaster,
    secondaryContainer = ColdTurkey,
    onSecondaryContainer = BleachedCedar,
    tertiary = ColdTurkey,
    onTertiary = BleachedCedar,
    background = Alabaster,
    onBackground = BleachedCedar,
    surface = Alabaster,
    onSurface = BleachedCedar,
    surfaceVariant = Color(0xFFEEEDED),
    onSurfaceVariant = BleachedCedar,
    outline = Color(0xFFCCCBCB),
    error = Cabaret,
    onError = Alabaster,
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldenGrass,
    onPrimary = BleachedCedar,
    primaryContainer = Color(0xFFB88A1F),
    onPrimaryContainer = Alabaster,
    secondary = Cabaret,
    onSecondary = Alabaster,
    secondaryContainer = Color(0xFFA83149),
    onSecondaryContainer = Alabaster,
    tertiary = ColdTurkey,
    onTertiary = BleachedCedar,
    background = BleachedCedar,
    onBackground = Alabaster,
    surface = BleachedCedar,
    onSurface = Alabaster,
    surfaceVariant = Color(0xFF3A2E3E),
    onSurfaceVariant = Alabaster,
    outline = Color(0xFF5A4E5E),
    error = Color(0xFFE65A7A),
    onError = Alabaster,
)

@Composable
fun GymRoutinesTheme(
    isDark: Boolean,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (isDark) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
