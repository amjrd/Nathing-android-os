package com.example.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.example.data.LauncherRepository
import com.example.model.ActiveDialog
import com.example.model.AppItem
import com.example.model.FolderItem
import com.example.model.HomeWidgetItem
import com.example.model.HomeWidgetType
import com.example.model.LauncherSettings
import com.example.model.MonochromeIconType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LauncherRepository(application.applicationContext)

    val settings: StateFlow<LauncherSettings> = repository.settings
    val allApps: StateFlow<List<AppItem>> = repository.allApps
    val dockApps: StateFlow<List<AppItem>> = repository.dockApps
    val homeWidgets: StateFlow<List<HomeWidgetItem>> = repository.homeWidgets
    val folders: StateFlow<List<FolderItem>> = repository.folders

    private val _isAppDrawerOpen = MutableStateFlow(false)
    val isAppDrawerOpen: StateFlow<Boolean> = _isAppDrawerOpen.asStateFlow()

    private val _activeDialog = MutableStateFlow<ActiveDialog>(ActiveDialog.None)
    val activeDialog: StateFlow<ActiveDialog> = _activeDialog.asStateFlow()

    fun openAppDrawer() {
        _isAppDrawerOpen.value = true
    }

    fun closeAppDrawer() {
        _isAppDrawerOpen.value = false
    }

    fun openCustomise() {
        _activeDialog.value = ActiveDialog.CustomiseSheet
    }

    fun openWidgetPicker() {
        _activeDialog.value = ActiveDialog.WidgetPicker
    }

    fun openFolder(folderId: String) {
        _activeDialog.value = ActiveDialog.FolderDetail(folderId)
    }

    fun dismissDialog() {
        _activeDialog.value = ActiveDialog.None
    }

    fun updateSettings(transform: (LauncherSettings) -> LauncherSettings) {
        repository.updateSettings(transform)
    }

    fun addWidget(type: HomeWidgetType, colSpan: Int, rowSpan: Int) {
        repository.addWidget(type, colSpan, rowSpan)
    }

    fun removeWidget(widgetId: String) {
        repository.removeWidget(widgetId)
    }

    fun toggleFolderEnlarged(folderId: String) {
        repository.toggleFolderEnlarged(folderId)
    }

    fun onWidgetClick(widget: HomeWidgetItem) {
        when (widget.type) {
            HomeWidgetType.DOT_CLOCK, HomeWidgetType.ANALOG_CLOCK -> {
                _activeDialog.value = ActiveDialog.ClockStopwatch
            }
            HomeWidgetType.WEATHER_METRIC -> {
                _activeDialog.value = ActiveDialog.WeatherDetail
            }
            HomeWidgetType.GLYPH_TORCH -> {
                _activeDialog.value = ActiveDialog.GlyphController
            }
            HomeWidgetType.VOICE_RECORDER -> {
                _activeDialog.value = ActiveDialog.VoiceRecorder
            }
            HomeWidgetType.QUICK_SETTINGS -> {
                _activeDialog.value = ActiveDialog.CustomiseSheet
            }
            else -> {
                // Interactive directly inside the widget (e.g. Media Player, Steps, Compass)
            }
        }
    }

    fun launchApp(app: AppItem) {
        val context = getApplication<Application>().applicationContext
        var launchedSuccessfully = false

        // 1. Try launching real package intent on the Android device
        try {
            val pm = context.packageManager
            val launchIntent = pm.getLaunchIntentForPackage(app.packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                launchedSuccessfully = true
            }
        } catch (_: Exception) {
            launchedSuccessfully = false
        }

        // 2. If not installed or if it's Nothing's iconic apps, route to experiential dialogs
        if (!launchedSuccessfully) {
            when (app.iconType) {
                MonochromeIconType.GLYPH_CONTROL -> _activeDialog.value = ActiveDialog.GlyphController
                MonochromeIconType.RECORDER -> _activeDialog.value = ActiveDialog.VoiceRecorder
                MonochromeIconType.WEATHER -> _activeDialog.value = ActiveDialog.WeatherDetail
                MonochromeIconType.CALCULATOR -> _activeDialog.value = ActiveDialog.Calculator
                MonochromeIconType.CLOCK -> _activeDialog.value = ActiveDialog.ClockStopwatch
                MonochromeIconType.SETTINGS -> _activeDialog.value = ActiveDialog.CustomiseSheet
                else -> {
                    // If no specific dialog, fall back to launcher settings or gentle feedback
                    _activeDialog.value = ActiveDialog.CustomiseSheet
                }
            }
        }
    }
}
