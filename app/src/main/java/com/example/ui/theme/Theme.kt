package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NothingColorScheme = darkColorScheme(
    primary = NothingWhite,
    onPrimary = NothingBlack,
    primaryContainer = NothingCardElevated,
    onPrimaryContainer = NothingWhite,
    secondary = NothingMuted,
    onSecondary = NothingWhite,
    secondaryContainer = NothingBorder,
    onSecondaryContainer = NothingWhite,
    tertiary = NothingRed,
    onTertiary = NothingWhite,
    background = NothingBlack,
    onBackground = NothingWhite,
    surface = NothingDarkSurface,
    onSurface = NothingWhite,
    surfaceVariant = NothingCardBg,
    onSurfaceVariant = NothingMuted,
    outline = NothingBorder,
    outlineVariant = NothingBorderSubtle
)

@Composable
fun NothingLauncherTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NothingColorScheme,
        typography = Typography,
        content = content
    )
}
