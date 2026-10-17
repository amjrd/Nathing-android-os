package com.example.data

import android.content.Context
import android.content.Intent
import com.example.model.AppItem
import com.example.model.FolderItem
import com.example.model.HomeWidgetItem
import com.example.model.HomeWidgetType
import com.example.model.IconPackStyle
import com.example.model.LauncherSettings
import com.example.model.MonochromeIconType
import com.example.model.WallpaperTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LauncherRepository(private val context: Context) {

    private val _settings = MutableStateFlow(LauncherSettings())
    val settings: StateFlow<LauncherSettings> = _settings.asStateFlow()

    private val _allApps = MutableStateFlow<List<AppItem>>(emptyList())
    val allApps: StateFlow<List<AppItem>> = _allApps.asStateFlow()

    private val _homeWidgets = MutableStateFlow<List<HomeWidgetItem>>(emptyList())
    val homeWidgets: StateFlow<List<HomeWidgetItem>> = _homeWidgets.asStateFlow()

    private val _folders = MutableStateFlow<List<FolderItem>>(emptyList())
    val folders: StateFlow<List<FolderItem>> = _folders.asStateFlow()

    private val _dockApps = MutableStateFlow<List<AppItem>>(emptyList())
    val dockApps: StateFlow<List<AppItem>> = _dockApps.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val defaultNothingApps = listOf(
            AppItem("phone", "Phone", "com.google.android.dialer", MonochromeIconType.PHONE, isDockApp = true),
            AppItem("messages", "Messages", "com.google.android.apps.messaging", MonochromeIconType.MESSAGES, isDockApp = true, badgeCount = 2),
            AppItem("camera", "Camera", "com.google.android.GoogleCamera", MonochromeIconType.CAMERA, isDockApp = true),
            AppItem("settings", "Settings", "com.android.settings", MonochromeIconType.SETTINGS, isDockApp = true),
            AppItem("glyph", "Glyph Interface", "com.nothing.glyph", MonochromeIconType.GLYPH_CONTROL, isFavorite = true),
            AppItem("recorder", "Recorder", "com.nothing.soundrecorder", MonochromeIconType.RECORDER, isFavorite = true),
            AppItem("weather", "Weather", "com.nothing.weather", MonochromeIconType.WEATHER, isFavorite = true),
            AppItem("clock", "Clock", "com.google.android.deskclock", MonochromeIconType.CLOCK, isFavorite = true),
            AppItem("calculator", "Calculator", "com.google.android.calculator", MonochromeIconType.CALCULATOR, isFavorite = true),
            AppItem("music", "Nothing X", "com.nothing.ear", MonochromeIconType.MUSIC, isFavorite = true),
            AppItem("files", "Files", "com.google.android.documentsui", MonochromeIconType.FILES),
            AppItem("gallery", "Photos", "com.google.android.apps.photos", MonochromeIconType.GALLERY),
            AppItem("browser", "Chrome", "com.android.chrome", MonochromeIconType.BROWSER),
            AppItem("notes", "Notes", "com.google.android.keep", MonochromeIconType.NOTES),
            AppItem("compass", "Compass", "com.nothing.compass", MonochromeIconType.COMPASS),
            AppItem("calendar", "Calendar", "com.google.android.calendar", MonochromeIconType.CALENDAR),
            AppItem("maps", "Maps", "com.google.android.apps.maps", MonochromeIconType.MAPS),
            AppItem("health", "Activity", "com.google.android.apps.fitness", MonochromeIconType.HEALTH),
            AppItem("store", "Play Store", "com.android.vending", MonochromeIconType.STORE)
        )

        // Try to query real installed launcher activities from PackageManager
        val installedAppsList = mutableListOf<AppItem>()
        try {
            val pm = context.packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolved = pm.queryIntentActivities(mainIntent, 0)
            val thisPackage = context.packageName

            for (info in resolved) {
                val pkg = info.activityInfo.packageName
                if (pkg == thisPackage) continue

                val label = info.loadLabel(pm).toString()
                val iconType = guessIconType(label, pkg)
                installedAppsList.add(
                    AppItem(
                        id = "pkg_$pkg",
                        name = label,
                        packageName = pkg,
                        iconType = iconType,
                        isInstalledApp = true
                    )
                )
            }
        } catch (_: Exception) {
            // Keep default list on any permission or environment fallback
        }

        // Combine: default Nothing OS branded apps first, then any extra installed apps
        val mergedApps = defaultNothingApps.toMutableList()
        for (installed in installedAppsList) {
            if (mergedApps.none { it.packageName == installed.packageName }) {
                mergedApps.add(installed)
            }
        }

        _allApps.value = mergedApps
        _dockApps.value = defaultNothingApps.filter { it.isDockApp }

        // Setup iconic Nothing OS initial widgets
        _homeWidgets.value = listOf(
            HomeWidgetItem("widget_clock", HomeWidgetType.DOT_CLOCK, colSpan = 4, rowSpan = 2, title = "N-Dot Clock"),
            HomeWidgetItem("widget_weather", HomeWidgetType.WEATHER_METRIC, colSpan = 2, rowSpan = 2, title = "Weather"),
            HomeWidgetItem("widget_toggles", HomeWidgetType.QUICK_SETTINGS, colSpan = 2, rowSpan = 2, title = "Quick Pods"),
            HomeWidgetItem("widget_media", HomeWidgetType.MEDIA_PLAYER, colSpan = 2, rowSpan = 2, title = "Nothing Audio"),
            HomeWidgetItem("widget_glyph", HomeWidgetType.GLYPH_TORCH, colSpan = 2, rowSpan = 2, title = "Glyph Torch"),
            HomeWidgetItem("widget_steps", HomeWidgetType.STEP_COUNTER, colSpan = 2, rowSpan = 2, title = "Activity")
        )

        // Setup iconic Nothing OS enlarged folder
        _folders.value = listOf(
            FolderItem(
                id = "folder_essentials",
                name = "MEDIA & TOOLS",
                appIds = listOf("recorder", "calculator", "music", "compass", "weather", "notes"),
                isEnlarged = true
            )
        )
    }

    private fun guessIconType(label: String, pkg: String): MonochromeIconType {
        val lower = "$label $pkg".lowercase()
        return when {
            "camera" in lower -> MonochromeIconType.CAMERA
            "phone" in lower || "dialer" in lower -> MonochromeIconType.PHONE
            "message" in lower || "sms" in lower || "chat" in lower -> MonochromeIconType.MESSAGES
            "browser" in lower || "chrome" in lower || "web" in lower -> MonochromeIconType.BROWSER
            "setting" in lower -> MonochromeIconType.SETTINGS
            "weather" in lower -> MonochromeIconType.WEATHER
            "clock" in lower || "alarm" in lower || "timer" in lower -> MonochromeIconType.CLOCK
            "record" in lower || "voice" in lower -> MonochromeIconType.RECORDER
            "calc" in lower -> MonochromeIconType.CALCULATOR
            "music" in lower || "audio" in lower || "sound" in lower -> MonochromeIconType.MUSIC
            "photo" in lower || "gallery" in lower || "image" in lower -> MonochromeIconType.GALLERY
            "file" in lower || "document" in lower -> MonochromeIconType.FILES
            "map" in lower || "nav" in lower -> MonochromeIconType.MAPS
            "store" in lower || "market" in lower || "vending" in lower -> MonochromeIconType.STORE
            "calendar" in lower -> MonochromeIconType.CALENDAR
            "note" in lower || "memo" in lower -> MonochromeIconType.NOTES
            "compass" in lower -> MonochromeIconType.COMPASS
            "fit" in lower || "health" in lower || "step" in lower -> MonochromeIconType.HEALTH
            "terminal" in lower || "code" in lower -> MonochromeIconType.TERMINAL
            else -> MonochromeIconType.GENERIC
        }
    }

    fun updateSettings(transform: (LauncherSettings) -> LauncherSettings) {
        _settings.update(transform)
    }

    fun toggleFolderEnlarged(folderId: String) {
        _folders.update { list ->
            list.map { if (it.id == folderId) it.copy(isEnlarged = !it.isEnlarged) else it }
        }
    }

    fun removeWidget(widgetId: String) {
        _homeWidgets.update { list -> list.filterNot { it.id == widgetId } }
    }

    fun addWidget(type: HomeWidgetType, colSpan: Int = 2, rowSpan: Int = 2) {
        val newWidget = HomeWidgetItem(
            id = "widget_${System.currentTimeMillis()}",
            type = type,
            colSpan = colSpan,
            rowSpan = rowSpan,
            title = when (type) {
                HomeWidgetType.DOT_CLOCK -> "N-Dot Clock"
                HomeWidgetType.ANALOG_CLOCK -> "Analog Clock"
                HomeWidgetType.WEATHER_METRIC -> "Weather"
                HomeWidgetType.QUICK_SETTINGS -> "Quick Pods"
                HomeWidgetType.MEDIA_PLAYER -> "Nothing Audio"
                HomeWidgetType.STEP_COUNTER -> "Activity Ring"
                HomeWidgetType.VOICE_RECORDER -> "Tape Recorder"
                HomeWidgetType.GLYPH_TORCH -> "Glyph Torch"
                HomeWidgetType.COMPASS -> "Compass"
            }
        )
        _homeWidgets.update { it + newWidget }
    }

    fun toggleAppFavorite(appId: String) {
        _allApps.update { list ->
            list.map { if (it.id == appId) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }
}
