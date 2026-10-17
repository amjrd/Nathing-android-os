package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppItem
import com.example.model.FolderItem
import com.example.model.HomeWidgetItem
import com.example.model.HomeWidgetType
import com.example.model.LauncherSettings
import com.example.ui.components.NothingAppIcon
import com.example.ui.components.NothingWallpaperBackground
import com.example.ui.components.NothingWidgetCard
import com.example.ui.theme.NothingBlack
import com.example.ui.theme.NothingBorder
import com.example.ui.theme.NothingCardBg
import com.example.ui.theme.NothingCardElevated
import com.example.ui.theme.NothingDotFontFamily
import com.example.ui.theme.NothingMonoFontFamily
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite

@Composable
fun HomeScreen(
    settings: LauncherSettings,
    allApps: List<AppItem>,
    dockApps: List<AppItem>,
    widgets: List<HomeWidgetItem>,
    folders: List<FolderItem>,
    onAppClick: (AppItem) -> Unit,
    onWidgetClick: (HomeWidgetItem) -> Unit,
    onFolderClick: (FolderItem) -> Unit,
    onOpenAppDrawer: () -> Unit,
    onOpenCustomise: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_root")
    ) {
        // Dynamic Nothing OS wallpaper background
        NothingWallpaperBackground(theme = settings.wallpaper)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Top Status Bar Glance
            TopGlanceBar(onOpenCustomise = onOpenCustomise)

            // Scrollable Home Feed (Widgets + Enlarged Folders + Favorite Apps)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Full-width N-Dot Clock Widget if present
                val fullWidthClock = widgets.find { it.type == HomeWidgetType.DOT_CLOCK }
                if (fullWidthClock != null) {
                    item(key = fullWidthClock.id) {
                        NothingWidgetCard(
                            widget = fullWidthClock,
                            modifier = Modifier.fillMaxWidth(),
                            onWidgetClick = { onWidgetClick(fullWidthClock) }
                        )
                    }
                }

                // 2. Pairs of 2-col widgets (e.g. Quick Settings + Weather)
                val pairWidgets = widgets.filter { it.type != HomeWidgetType.DOT_CLOCK }
                val widgetChunks = pairWidgets.chunked(2)

                items(widgetChunks) { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { w ->
                            NothingWidgetCard(
                                widget = w,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(140.dp),
                                onWidgetClick = { onWidgetClick(w) }
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                // 3. Iconic Enlarged Nothing OS Folders (2x2 with direct app launch)
                items(folders) { folder ->
                    EnlargedNothingFolder(
                        folder = folder,
                        allApps = allApps,
                        iconStyle = settings.iconPack,
                        onAppClick = onAppClick,
                        onOpenFolderDetail = { onFolderClick(folder) }
                    )
                }

                // 4. Favorite apps row on home screen
                val homeFavorites = allApps.filter { it.isFavorite }.take(4)
                if (homeFavorites.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(NothingCardBg)
                                .border(1.dp, NothingBorder, RoundedCornerShape(24.dp))
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            homeFavorites.forEach { app ->
                                NothingAppIcon(
                                    app = app,
                                    style = settings.iconPack,
                                    showLabel = settings.showLabels,
                                    onClick = { onAppClick(app) }
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Bottom Fixed Dock & App Drawer Trigger
            BottomDockSection(
                dockApps = dockApps,
                settings = settings,
                onAppClick = onAppClick,
                onOpenAppDrawer = onOpenAppDrawer
            )
        }
    }
}

@Composable
private fun TopGlanceBar(
    onOpenCustomise: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(NothingRed)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "NOTHING OS",
                fontFamily = NothingDotFontFamily,
                fontSize = 11.sp,
                color = NothingWhite
            )
        }

        // Customise Button Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(NothingCardBg)
                .border(1.dp, NothingBorder, RoundedCornerShape(14.dp))
                .clickable { onOpenCustomise() }
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Customise",
                    tint = NothingWhite,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "CUSTOMISE",
                    fontFamily = NothingMonoFontFamily,
                    fontSize = 9.sp,
                    color = NothingWhite
                )
            }
        }
    }
}

/**
 * Iconic Nothing OS 2x2 Enlarged Folder Component.
 * The top 3 apps can be tapped directly, and the 4th slot acts as an overflow indicator.
 */
@Composable
fun EnlargedNothingFolder(
    folder: FolderItem,
    allApps: List<AppItem>,
    iconStyle: com.example.model.IconPackStyle,
    onAppClick: (AppItem) -> Unit,
    onOpenFolderDetail: () -> Unit
) {
    val folderApps = folder.appIds.mapNotNull { id -> allApps.find { it.id == id } }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(NothingCardBg)
            .border(1.dp, NothingBorder, RoundedCornerShape(26.dp))
            .padding(14.dp)
    ) {
        Column {
            // Folder title row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenFolderDetail() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = folder.name,
                    fontFamily = NothingDotFontFamily,
                    fontSize = 11.sp,
                    color = NothingWhite
                )
                Text(
                    text = "${folderApps.size} APPS ›",
                    fontFamily = NothingMonoFontFamily,
                    fontSize = 9.sp,
                    color = NothingMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4 direct action app slots inside the enlarged folder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val directApps = folderApps.take(3)
                directApps.forEach { app ->
                    NothingAppIcon(
                        app = app,
                        style = iconStyle,
                        showLabel = true,
                        onClick = { onAppClick(app) }
                    )
                }

                // Overflow / Open all slot
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onOpenFolderDetail() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(NothingCardElevated)
                            .border(1.dp, NothingBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${(folderApps.size - 3).coerceAtLeast(1)}",
                            fontFamily = NothingDotFontFamily,
                            fontSize = 12.sp,
                            color = NothingRed
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "MORE",
                        fontFamily = NothingMonoFontFamily,
                        fontSize = 10.sp,
                        color = NothingMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomDockSection(
    dockApps: List<AppItem>,
    settings: LauncherSettings,
    onAppClick: (AppItem) -> Unit,
    onOpenAppDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Swipe Up Indicator with N-Dot typography
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { onOpenAppDrawer() }
                .padding(horizontal = 12.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Swipe up for all apps",
                tint = NothingMuted,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "SWIPE UP FOR APPS",
                fontFamily = NothingDotFontFamily,
                fontSize = 9.sp,
                color = NothingMuted
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Monochromatic App Dock
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xE0111111))
                .border(1.dp, NothingBorder, RoundedCornerShape(28.dp))
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            dockApps.forEach { app ->
                NothingAppIcon(
                    app = app,
                    style = settings.iconPack,
                    showLabel = false,
                    iconSize = 52.dp,
                    onClick = { onAppClick(app) }
                )
            }
        }
    }
}
