package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppItem
import com.example.model.IconPackStyle
import com.example.model.MonochromeIconType
import com.example.ui.theme.NothingBlack
import com.example.ui.theme.NothingBorder
import com.example.ui.theme.NothingCardBg
import com.example.ui.theme.NothingMonoFontFamily
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NothingAppIcon(
    app: AppItem,
    modifier: Modifier = Modifier,
    iconSize: Dp = 56.dp,
    style: IconPackStyle = IconPackStyle.NOTHING_MONOCHROME,
    showLabel: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .testTag("app_icon_${app.id}")
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = iconSize / 2f + 8.dp),
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(iconSize),
            contentAlignment = Alignment.Center
        ) {
            when (style) {
                IconPackStyle.NOTHING_MONOCHROME -> {
                    // Classic Nothing OS circular matte-black background with subtle high-contrast border
                    Box(
                        modifier = Modifier
                            .size(iconSize)
                            .clip(CircleShape)
                            .background(NothingCardBg)
                            .border(1.dp, NothingBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AppGlyphIcon(type = app.iconType, tint = NothingWhite, size = iconSize * 0.52f)
                    }
                }
                IconPackStyle.MINIMAL_OUTLINE -> {
                    // Minimalist wireframe outline icon
                    Box(
                        modifier = Modifier
                            .size(iconSize)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .border(1.5.dp, NothingWhite, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AppGlyphIcon(type = app.iconType, tint = NothingWhite, size = iconSize * 0.5f)
                    }
                }
                IconPackStyle.NOTHING_DOTS -> {
                    // Circular icon with dotted perimeter
                    Box(
                        modifier = Modifier.size(iconSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(iconSize)) {
                            val r = size.minDimension / 2f - 3f
                            val center = Offset(size.width / 2f, size.height / 2f)
                            drawCircle(color = NothingBlack, radius = r)
                            // Draw 24 perimeter dots
                            val dotCount = 24
                            for (i in 0 until dotCount) {
                                val angle = (i * 2 * Math.PI / dotCount).toFloat()
                                val x = center.x + r * cos(angle)
                                val y = center.y + r * sin(angle)
                                drawCircle(color = Color(0x66FFFFFF), radius = 1.5.dp.toPx(), center = Offset(x, y))
                            }
                        }
                        AppGlyphIcon(type = app.iconType, tint = NothingWhite, size = iconSize * 0.5f)
                    }
                }
            }

            // Nothing Red Notification Badge / Indicator dot
            if (app.badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 2.dp)
                        .clip(CircleShape)
                        .background(NothingRed)
                        .border(1.5.dp, NothingBlack, CircleShape)
                )
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = app.name,
                color = NothingWhite,
                fontSize = 11.sp,
                fontFamily = NothingMonoFontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AppGlyphIcon(
    type: MonochromeIconType,
    tint: Color = NothingWhite,
    size: Dp = 26.dp
) {
    val imageVector: ImageVector = when (type) {
        MonochromeIconType.CAMERA -> Icons.Filled.CameraAlt
        MonochromeIconType.PHONE -> Icons.Filled.Phone
        MonochromeIconType.MESSAGES -> Icons.Filled.ChatBubble
        MonochromeIconType.BROWSER -> Icons.Filled.Language
        MonochromeIconType.SETTINGS -> Icons.Filled.Settings
        MonochromeIconType.WEATHER -> Icons.Filled.WbSunny
        MonochromeIconType.CLOCK -> Icons.Filled.Schedule
        MonochromeIconType.RECORDER -> Icons.Filled.Mic
        MonochromeIconType.CALCULATOR -> Icons.Filled.Calculate
        MonochromeIconType.MUSIC -> Icons.Filled.Headphones
        MonochromeIconType.GALLERY -> Icons.Filled.PhotoLibrary
        MonochromeIconType.FILES -> Icons.Filled.Folder
        MonochromeIconType.GLYPH_CONTROL -> Icons.Filled.Lightbulb
        MonochromeIconType.COMPASS -> Icons.Filled.Explore
        MonochromeIconType.NOTES -> Icons.Filled.NoteAlt
        MonochromeIconType.STORE -> Icons.Filled.ShoppingBag
        MonochromeIconType.MAPS -> Icons.Filled.Map
        MonochromeIconType.CALENDAR -> Icons.Filled.CalendarMonth
        MonochromeIconType.TERMINAL -> Icons.Filled.Terminal
        MonochromeIconType.HEALTH -> Icons.Outlined.FitnessCenter
        MonochromeIconType.GENERIC -> Icons.Filled.Widgets
    }

    Icon(
        imageVector = imageVector,
        contentDescription = type.name,
        tint = tint,
        modifier = Modifier.size(size)
    )
}
