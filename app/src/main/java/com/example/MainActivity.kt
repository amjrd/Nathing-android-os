package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.ActiveDialog
import com.example.ui.dialogs.CalculatorDialog
import com.example.ui.dialogs.ClockStopwatchDialog
import com.example.ui.dialogs.CustomiseSheet
import com.example.ui.dialogs.FolderDetailDialog
import com.example.ui.dialogs.GlyphControllerDialog
import com.example.ui.dialogs.VoiceRecorderDialog
import com.example.ui.dialogs.WeatherDetailDialog
import com.example.ui.dialogs.WidgetPickerDialog
import com.example.ui.screens.AppDrawerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.NothingLauncherTheme
import com.example.viewmodel.LauncherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NothingLauncherTheme {
                NothingLauncherApp()
            }
        }
    }
}

@Composable
fun NothingLauncherApp(
    viewModel: LauncherViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val allApps by viewModel.allApps.collectAsStateWithLifecycle()
    val dockApps by viewModel.dockApps.collectAsStateWithLifecycle()
    val widgets by viewModel.homeWidgets.collectAsStateWithLifecycle()
    val folders by viewModel.folders.collectAsStateWithLifecycle()
    val isAppDrawerOpen by viewModel.isAppDrawerOpen.collectAsStateWithLifecycle()
    val activeDialog by viewModel.activeDialog.collectAsStateWithLifecycle()

    // Handle back button according to standard launcher UX
    BackHandler(enabled = isAppDrawerOpen || activeDialog !is ActiveDialog.None) {
        if (activeDialog !is ActiveDialog.None) {
            viewModel.dismissDialog()
        } else if (isAppDrawerOpen) {
            viewModel.closeAppDrawer()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Home Screen with widgets, enlarged folders, and dock
        HomeScreen(
            settings = settings,
            allApps = allApps,
            dockApps = dockApps,
            widgets = widgets,
            folders = folders,
            onAppClick = { app -> viewModel.launchApp(app) },
            onWidgetClick = { widget -> viewModel.onWidgetClick(widget) },
            onFolderClick = { folder -> viewModel.openFolder(folder.id) },
            onOpenAppDrawer = { viewModel.openAppDrawer() },
            onOpenCustomise = { viewModel.openCustomise() }
        )

        // 2. Slide-up App Drawer with search
        AnimatedVisibility(
            visible = isAppDrawerOpen,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            AppDrawerScreen(
                apps = allApps,
                settings = settings,
                onAppClick = { app -> viewModel.launchApp(app) },
                onCloseDrawer = { viewModel.closeAppDrawer() }
            )
        }

        // 3. Modals and experiential dialogs
        when (val dialog = activeDialog) {
            is ActiveDialog.None -> {}
            is ActiveDialog.GlyphController -> {
                GlyphControllerDialog(onDismiss = { viewModel.dismissDialog() })
            }
            is ActiveDialog.VoiceRecorder -> {
                VoiceRecorderDialog(onDismiss = { viewModel.dismissDialog() })
            }
            is ActiveDialog.WeatherDetail -> {
                WeatherDetailDialog(onDismiss = { viewModel.dismissDialog() })
            }
            is ActiveDialog.Calculator -> {
                CalculatorDialog(onDismiss = { viewModel.dismissDialog() })
            }
            is ActiveDialog.ClockStopwatch -> {
                ClockStopwatchDialog(onDismiss = { viewModel.dismissDialog() })
            }
            is ActiveDialog.CustomiseSheet -> {
                CustomiseSheet(
                    settings = settings,
                    onUpdateSettings = { transform -> viewModel.updateSettings(transform) },
                    onOpenWidgetPicker = { viewModel.openWidgetPicker() },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is ActiveDialog.WidgetPicker -> {
                WidgetPickerDialog(
                    onSelectWidget = { type, colSpan, rowSpan ->
                        viewModel.addWidget(type, colSpan, rowSpan)
                    },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is ActiveDialog.FolderDetail -> {
                val folder = folders.find { it.id == dialog.folderId }
                if (folder != null) {
                    FolderDetailDialog(
                        folder = folder,
                        allApps = allApps,
                        iconStyle = settings.iconPack,
                        onToggleEnlarged = { viewModel.toggleFolderEnlarged(folder.id) },
                        onAppClick = { app -> viewModel.launchApp(app) },
                        onDismiss = { viewModel.dismissDialog() }
                    )
                } else {
                    viewModel.dismissDialog()
                }
            }
        }
    }
}
