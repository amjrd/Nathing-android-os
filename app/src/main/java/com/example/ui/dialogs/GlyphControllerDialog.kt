package com.example.ui.dialogs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.GlyphGlow
import com.example.ui.theme.GlyphOff
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
fun GlyphControllerDialog(
    onDismiss: () -> Unit
) {
    var isGlyphMasterOn by remember { mutableStateOf(true) }
    var glyphBrightness by remember { mutableFloatStateOf(0.85f) }
    var flipToGlyph by remember { mutableStateOf(true) }
    var musicVisualizerMode by remember { mutableStateOf(false) }
    var batteryIndicatorMode by remember { mutableStateOf(true) }
    var strobeEffect by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "glyph_strobe")
    val strobeAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(120, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "strobe_alpha"
    )

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
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(NothingRed)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GLYPH INTERFACE",
                                fontFamily = NothingDotFontFamily,
                                fontSize = 14.sp,
                                color = NothingWhite
                            )
                        }
                        Text(
                            text = "NOTHING (R) HARDWARE LIGHTING",
                            fontFamily = NothingMonoFontFamily,
                            fontSize = 9.sp,
                            color = NothingMuted
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("glyph_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = NothingWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Realistic Animated Nothing Phone Glyph Back Schematic
                val effectiveAlpha = if (!isGlyphMasterOn) 0f
                else if (strobeEffect) strobeAlpha
                else glyphBrightness

                Box(
                    modifier = Modifier
                        .size(170.dp, 240.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF070707))
                        .border(1.5.dp, Color(0xFF262626), RoundedCornerShape(32.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val glowColor = GlyphGlow.copy(alpha = effectiveAlpha)
                        val strokeW = 4.dp.toPx()

                        // 1. Top camera halo ring
                        drawCircle(
                            color = if (isGlyphMasterOn) glowColor else GlyphOff,
                            radius = 24.dp.toPx(),
                            center = Offset(size.width * 0.35f, size.height * 0.28f),
                            style = Stroke(width = strokeW)
                        )

                        // 2. Top-right diagonal slash
                        drawLine(
                            color = if (isGlyphMasterOn) glowColor else GlyphOff,
                            start = Offset(size.width * 0.65f, size.height * 0.12f),
                            end = Offset(size.width * 0.88f, size.height * 0.24f),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )

                        // 3. Central wireless coil glyph
                        drawCircle(
                            color = if (isGlyphMasterOn) glowColor else GlyphOff,
                            radius = 36.dp.toPx(),
                            center = Offset(size.width * 0.5f, size.height * 0.58f),
                            style = Stroke(width = strokeW)
                        )

                        // 4. Center-right coil notch segment
                        drawLine(
                            color = if (isGlyphMasterOn) glowColor else GlyphOff,
                            start = Offset(size.width * 0.76f, size.height * 0.52f),
                            end = Offset(size.width * 0.88f, size.height * 0.52f),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )

                        // 5. Bottom battery indicator exclamation line + dot
                        drawLine(
                            color = if (isGlyphMasterOn) glowColor else GlyphOff,
                            start = Offset(size.width * 0.5f, size.height * 0.82f),
                            end = Offset(size.width * 0.5f, size.height * 0.92f),
                            strokeWidth = strokeW,
                            cap = StrokeCap.Round
                        )
                        drawCircle(
                            color = if (isGlyphMasterOn) glowColor else GlyphOff,
                            radius = 2.5.dp.toPx(),
                            center = Offset(size.width * 0.5f, size.height * 0.96f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Master Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NothingCardElevated)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = null,
                            tint = if (isGlyphMasterOn) NothingRed else NothingMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "GLYPH LIGHTS",
                                fontFamily = NothingMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NothingWhite
                            )
                            Text(
                                text = if (isGlyphMasterOn) "Active & synced" else "Disabled",
                                fontFamily = NothingMonoFontFamily,
                                fontSize = 10.sp,
                                color = NothingMuted
                            )
                        }
                    }

                    Switch(
                        checked = isGlyphMasterOn,
                        onCheckedChange = { isGlyphMasterOn = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NothingWhite,
                            checkedTrackColor = NothingRed,
                            uncheckedThumbColor = NothingMuted,
                            uncheckedTrackColor = Color(0xFF222222)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Brightness Slider
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NothingCardElevated)
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "BRIGHTNESS",
                            fontFamily = NothingMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = NothingWhite
                        )
                        Text(
                            text = "${(glyphBrightness * 100).toInt()}%",
                            fontFamily = NothingDotFontFamily,
                            fontSize = 11.sp,
                            color = NothingRed
                        )
                    }
                    Slider(
                        value = glyphBrightness,
                        onValueChange = { glyphBrightness = it },
                        enabled = isGlyphMasterOn,
                        colors = SliderDefaults.colors(
                            thumbColor = NothingWhite,
                            activeTrackColor = NothingWhite,
                            inactiveTrackColor = NothingBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Toggles: Strobe, Music Visualizer, Flip-to-Glyph
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NothingCardElevated)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    GlyphSettingRow(
                        title = "STROBE / FLASH MODE",
                        subtitle = "Rapid pulsing sequence",
                        icon = Icons.Default.FlashOn,
                        checked = strobeEffect,
                        enabled = isGlyphMasterOn,
                        onCheckedChange = { strobeEffect = it }
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(NothingBorder))
                    GlyphSettingRow(
                        title = "MUSIC VISUALISATION",
                        subtitle = "Glyph reacts to speaker audio",
                        icon = Icons.Default.MusicNote,
                        checked = musicVisualizerMode,
                        enabled = isGlyphMasterOn,
                        onCheckedChange = { musicVisualizerMode = it }
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(NothingBorder))
                    GlyphSettingRow(
                        title = "FLIP TO GLYPH",
                        subtitle = "Silent notifications face down",
                        icon = Icons.Default.Tune,
                        checked = flipToGlyph,
                        enabled = isGlyphMasterOn,
                        onCheckedChange = { flipToGlyph = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun GlyphSettingRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (enabled && checked) NothingWhite else NothingMuted,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontFamily = NothingMonoFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) NothingWhite else NothingMuted
                )
                Text(
                    text = subtitle,
                    fontFamily = NothingMonoFontFamily,
                    fontSize = 9.sp,
                    color = NothingMuted
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NothingWhite,
                checkedTrackColor = NothingRed,
                uncheckedThumbColor = NothingMuted,
                uncheckedTrackColor = Color(0xFF222222)
            )
        )
    }
}
