package com.example.model

enum class MonochromeIconType {
    CAMERA,
    PHONE,
    MESSAGES,
    BROWSER,
    SETTINGS,
    WEATHER,
    CLOCK,
    RECORDER,
    CALCULATOR,
    MUSIC,
    GALLERY,
    FILES,
    GLYPH_CONTROL,
    COMPASS,
    NOTES,
    STORE,
    MAPS,
    CALENDAR,
    TERMINAL,
    HEALTH,
    GENERIC
}

data class AppItem(
    val id: String,
    val name: String,
    val packageName: String,
    val iconType: MonochromeIconType,
    val isInstalledApp: Boolean = false,
    val isFavorite: Boolean = false,
    val isDockApp: Boolean = false,
    val badgeCount: Int = 0,
    val customDescription: String = ""
)

enum class HomeWidgetType {
    DOT_CLOCK,
    ANALOG_CLOCK,
    WEATHER_METRIC,
    QUICK_SETTINGS,
    MEDIA_PLAYER,
    STEP_COUNTER,
    VOICE_RECORDER,
    GLYPH_TORCH,
    COMPASS
}

data class HomeWidgetItem(
    val id: String,
    val type: HomeWidgetType,
    val colSpan: Int = 2, // in 4-column grid (2 = half width, 4 = full width)
    val rowSpan: Int = 2,
    val title: String = ""
)

data class FolderItem(
    val id: String,
    val name: String,
    val appIds: List<String>,
    val isEnlarged: Boolean = true // Nothing OS enlarged folder feature
)

enum class IconPackStyle {
    NOTHING_MONOCHROME, // Circular black fill with pure white glyph + red accent
    MINIMAL_OUTLINE,    // Wireframe circular outline with centered glyph
    NOTHING_DOTS        // Micro dot-matrix styled glyph
}

enum class WallpaperTheme {
    DOT_MATRIX_DARK,   // Subtle matrix of dots on AMOLED black
    GLYPH_NEON,        // Nothing Phone transparent schematic with glowing glyph contours
    MATTE_OBSIDIAN,    // Ultra deep pitch black with subtle glass reflection
    CIRCUIT_GLASS      // Translucent industrial tech schematic
}

data class LauncherSettings(
    val iconPack: IconPackStyle = IconPackStyle.NOTHING_MONOCHROME,
    val wallpaper: WallpaperTheme = WallpaperTheme.DOT_MATRIX_DARK,
    val showLabels: Boolean = true,
    val gridColumns: Int = 4,
    val hapticFeedback: Boolean = true,
    val monochromeOnly: Boolean = true
)

sealed interface ActiveDialog {
    data object None : ActiveDialog
    data object GlyphController : ActiveDialog
    data object VoiceRecorder : ActiveDialog
    data object WeatherDetail : ActiveDialog
    data object Calculator : ActiveDialog
    data object ClockStopwatch : ActiveDialog
    data object CustomiseSheet : ActiveDialog
    data object WidgetPicker : ActiveDialog
    data class FolderDetail(val folderId: String) : ActiveDialog
}
