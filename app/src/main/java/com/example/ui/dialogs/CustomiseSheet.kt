package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.IconPackStyle
import com.example.model.LauncherSettings
import com.example.model.WallpaperTheme
import com.example.ui.theme.NothingBlack
import com.example.ui.theme.NothingBorder
import com.example.ui.theme.NothingCardBg
import com.example.ui.theme.NothingCardElevated
import com.example.ui.theme.NothingDotFontFamily
import com.example.ui.theme.NothingMonoFontFamily
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingSubtle
import com.example.ui.theme.NothingWhite

@Composable
fun CustomiseSheet(
    settings: LauncherSettings,
    onUpdateSettings: ((LauncherSettings) -> LauncherSettings) -> Unit,
    onOpenWidgetPicker: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NothingBlack.copy(alpha = 0.96f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(NothingCardBg)
                    .border(1.dp, NothingBorder, RoundedCornerShape(28.dp))
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(NothingRed)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CUSTOMISE LAUNCHER",
                            fontFamily = NothingDotFontFamily,
                            fontSize = 13.sp,
                            color = NothingWhite
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("customise_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = NothingWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 1: Wallpapers
                SectionHeader("NOTHING WALLPAPERS")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WallpaperOption(
                        name = "DOTS",
                        sub = "Matrix",
                        selected = settings.wallpaper == WallpaperTheme.DOT_MATRIX_DARK,
                        modifier = Modifier.weight(1f)
                    ) {
                        onUpdateSettings { it.copy(wallpaper = WallpaperTheme.DOT_MATRIX_DARK) }
                    }
                    WallpaperOption(
                        name = "GLYPH",
                        sub = "Neon",
                        selected = settings.wallpaper == WallpaperTheme.GLYPH_NEON,
                        modifier = Modifier.weight(1f)
                    ) {
                        onUpdateSettings { it.copy(wallpaper = WallpaperTheme.GLYPH_NEON) }
                    }
                    WallpaperOption(
                        name = "MATTE",
                        sub = "Obsidian",
                        selected = settings.wallpaper == WallpaperTheme.MATTE_OBSIDIAN,
                        modifier = Modifier.weight(1f)
                    ) {
                        onUpdateSettings { it.copy(wallpaper = WallpaperTheme.MATTE_OBSIDIAN) }
                    }
                    WallpaperOption(
                        name = "GLASS",
                        sub = "Circuit",
                        selected = settings.wallpaper == WallpaperTheme.CIRCUIT_GLASS,
                        modifier = Modifier.weight(1f)
                    ) {
                        onUpdateSettings { it.copy(wallpaper = WallpaperTheme.CIRCUIT_GLASS) }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 2: Icon Pack Style
                SectionHeader("MONOCHROME ICON PACK")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NothingCardElevated)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    IconPackRow(
                        title = "NOTHING MONOCHROME",
                        subtitle = "Classic solid black & crisp white glyphs",
                        selected = settings.iconPack == IconPackStyle.NOTHING_MONOCHROME,
                        onClick = { onUpdateSettings { it.copy(iconPack = IconPackStyle.NOTHING_MONOCHROME) } }
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(NothingBorder))
                    IconPackRow(
                        title = "MINIMAL OUTLINE",
                        subtitle = "Clean wireframe circular glyphs",
                        selected = settings.iconPack == IconPackStyle.MINIMAL_OUTLINE,
                        onClick = { onUpdateSettings { it.copy(iconPack = IconPackStyle.MINIMAL_OUTLINE) } }
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(NothingBorder))
                    IconPackRow(
                        title = "NOTHING DOTS",
                        subtitle = "Dotted circular perimeter rings",
                        selected = settings.iconPack == IconPackStyle.NOTHING_DOTS,
                        onClick = { onUpdateSettings { it.copy(iconPack = IconPackStyle.NOTHING_DOTS) } }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 3: Layout & Labels
                SectionHeader("DESKTOP GRID & LABELS")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NothingCardElevated)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "APP LABELS",
                            fontFamily = NothingMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = NothingWhite
                        )
                        Text(
                            text = "Display text names below icons",
                            fontFamily = NothingMonoFontFamily,
                            fontSize = 9.sp,
                            color = NothingMuted
                        )
                    }
                    Switch(
                        checked = settings.showLabels,
                        onCheckedChange = { ch -> onUpdateSettings { it.copy(showLabels = ch) } },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NothingWhite,
                            checkedTrackColor = NothingRed,
                            uncheckedThumbColor = NothingMuted,
                            uncheckedTrackColor = Color(0xFF222222)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 4: Add Widgets Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(NothingWhite)
                        .clickable {
                            onDismiss()
                            onOpenWidgetPicker()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = NothingBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ADD WIDGET TO HOME",
                            fontFamily = NothingDotFontFamily,
                            fontSize = 11.sp,
                            color = NothingBlack
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontFamily = NothingDotFontFamily,
        fontSize = 10.sp,
        color = NothingWhite,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
private fun WallpaperOption(
    name: String,
    sub: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) Color(0xFF222222) else NothingCardElevated)
            .border(
                1.5.dp,
                if (selected) NothingRed else NothingBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = name,
                fontFamily = NothingDotFontFamily,
                fontSize = 10.sp,
                color = if (selected) NothingWhite else NothingMuted
            )
            Text(
                text = sub,
                fontFamily = NothingMonoFontFamily,
                fontSize = 8.sp,
                color = NothingSubtle
            )
        }
    }
}

@Composable
private fun IconPackRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontFamily = NothingMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = if (selected) NothingWhite else NothingMuted
            )
            Text(
                text = subtitle,
                fontFamily = NothingMonoFontFamily,
                fontSize = 9.sp,
                color = NothingSubtle
            )
        }
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(if (selected) NothingRed else Color.Transparent)
                .border(1.5.dp, if (selected) NothingRed else NothingBorder, CircleShape)
        )
    }
}
