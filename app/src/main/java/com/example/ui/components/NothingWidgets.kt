package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HomeWidgetItem
import com.example.model.HomeWidgetType
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
import com.example.ui.theme.NothingSubtle
import com.example.ui.theme.NothingWhite
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NothingWidgetCard(
    widget: HomeWidgetItem,
    modifier: Modifier = Modifier,
    onWidgetClick: () -> Unit
) {
    Box(
        modifier = modifier
            .testTag("widget_${widget.type.name.lowercase()}")
            .clip(RoundedCornerShape(24.dp))
            .background(NothingCardBg)
            .border(1.dp, NothingBorder, RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onWidgetClick
            )
            .padding(14.dp)
    ) {
        when (widget.type) {
            HomeWidgetType.DOT_CLOCK -> DotClockWidget()
            HomeWidgetType.ANALOG_CLOCK -> AnalogClockWidget()
            HomeWidgetType.WEATHER_METRIC -> WeatherMetricWidget()
            HomeWidgetType.QUICK_SETTINGS -> QuickSettingsPodsWidget()
            HomeWidgetType.MEDIA_PLAYER -> MediaPlayerWidget()
            HomeWidgetType.STEP_COUNTER -> ActivityStepsWidget()
            HomeWidgetType.VOICE_RECORDER -> VoiceRecorderWidget()
            HomeWidgetType.GLYPH_TORCH -> GlyphTorchWidget()
            HomeWidgetType.COMPASS -> CompassWidget()
        }
    }
}

/**
 * 1. Big Iconic Dot-Matrix Digital Clock Widget (4-col wide)
 */
@Composable
fun DotClockWidget() {
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            delay(1000)
        }
    }

    val hours = SimpleDateFormat("HH", Locale.getDefault()).format(currentTime.time)
    val minutes = SimpleDateFormat("mm", Locale.getDefault()).format(currentTime.time)
    val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(currentTime.time).uppercase()
    val dateStr = SimpleDateFormat("d MMM", Locale.getDefault()).format(currentTime.time).uppercase()

    val infiniteTransition = rememberInfiniteTransition(label = "colon_blink")
    val colonAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colon_blink_anim"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day and Date in Nothing typography
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(NothingRed)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$dayOfWeek, $dateStr",
                    fontFamily = NothingDotFontFamily,
                    fontSize = 11.sp,
                    color = NothingWhite
                )
            }

            // Weather pill indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(NothingCardElevated)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = NothingWhite,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "21°C",
                    fontFamily = NothingDotFontFamily,
                    fontSize = 10.sp,
                    color = NothingWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Large 5x7 Dot Matrix Clock Display
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DotMatrixChar(char = hours[0], dotSize = 4.dp, dotSpacing = 2.dp)
            Spacer(modifier = Modifier.width(4.dp))
            DotMatrixChar(char = hours[1], dotSize = 4.dp, dotSpacing = 2.dp)
            Spacer(modifier = Modifier.width(6.dp))

            // Iconic Nothing Red blinking colon
            DotMatrixChar(
                char = ':',
                dotSize = 4.dp,
                dotSpacing = 2.dp,
                activeColor = NothingRed.copy(alpha = colonAlpha)
            )

            Spacer(modifier = Modifier.width(6.dp))
            DotMatrixChar(char = minutes[0], dotSize = 4.dp, dotSpacing = 2.dp)
            Spacer(modifier = Modifier.width(4.dp))
            DotMatrixChar(char = minutes[1], dotSize = 4.dp, dotSpacing = 2.dp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "NOTHING OS (2.6) • LONDON",
            fontFamily = NothingMonoFontFamily,
            fontSize = 9.sp,
            color = NothingMuted
        )
    }
}

/**
 * 2. Minimalist Nothing OS Analog Clock Widget
 */
@Composable
fun AnalogClockWidget() {
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            delay(1000)
        }
    }

    val hour = currentTime.get(Calendar.HOUR)
    val minute = currentTime.get(Calendar.MINUTE)
    val second = currentTime.get(Calendar.SECOND)

    val hourAngle = (hour * 30f) + (minute * 0.5f)
    val minuteAngle = minute * 6f
    val secondAngle = second * 6f

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(105.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension / 2f - 4.dp.toPx()

                // Draw hour dots
                for (i in 0 until 12) {
                    val angle = Math.toRadians((i * 30).toDouble())
                    val dotDist = radius - 6.dp.toPx()
                    val dx = (center.x + dotDist * sin(angle)).toFloat()
                    val dy = (center.y - dotDist * cos(angle)).toFloat()
                    val isCardinal = (i % 3 == 0)
                    drawCircle(
                        color = if (isCardinal) NothingWhite else NothingSubtle,
                        radius = if (isCardinal) 2.5.dp.toPx() else 1.2.dp.toPx(),
                        center = Offset(dx, dy)
                    )
                }

                // Center pivot ring
                drawCircle(color = NothingWhite, radius = 3.dp.toPx(), center = center)
                drawCircle(color = NothingRed, radius = 1.5.dp.toPx(), center = center)

                // Hour Hand
                val hourRad = Math.toRadians(hourAngle.toDouble())
                val hourLen = radius * 0.5f
                drawLine(
                    color = NothingWhite,
                    start = center,
                    end = Offset(
                        (center.x + hourLen * sin(hourRad)).toFloat(),
                        (center.y - hourLen * cos(hourRad)).toFloat()
                    ),
                    strokeWidth = 3.5.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Minute Hand
                val minRad = Math.toRadians(minuteAngle.toDouble())
                val minLen = radius * 0.72f
                drawLine(
                    color = NothingWhite,
                    start = center,
                    end = Offset(
                        (center.x + minLen * sin(minRad)).toFloat(),
                        (center.y - minLen * cos(minRad)).toFloat()
                    ),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Second Hand (Iconic Nothing Red with tail)
                val secRad = Math.toRadians(secondAngle.toDouble())
                val secLen = radius * 0.82f
                val tailLen = radius * 0.2f
                drawLine(
                    color = NothingRed,
                    start = Offset(
                        (center.x - tailLen * sin(secRad)).toFloat(),
                        (center.y + tailLen * cos(secRad)).toFloat()
                    ),
                    end = Offset(
                        (center.x + secLen * sin(secRad)).toFloat(),
                        (center.y - secLen * cos(secRad)).toFloat()
                    ),
                    strokeWidth = 1.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "ANALOG",
            fontFamily = NothingMonoFontFamily,
            fontSize = 9.sp,
            color = NothingMuted
        )
    }
}

/**
 * 3. Nothing Weather Widget
 */
@Composable
fun WeatherMetricWidget() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LONDON",
                fontFamily = NothingDotFontFamily,
                fontSize = 11.sp,
                color = NothingWhite
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(NothingRed)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            DotMatrixString(
                text = "21°",
                dotSize = 3.dp,
                dotSpacing = 1.5.dp,
                activeColor = NothingWhite
            )
        }

        Column {
            Text(
                text = "PARTLY CLOUDY",
                fontFamily = NothingMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = NothingWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "H:24° L:16°",
                    fontFamily = NothingMonoFontFamily,
                    fontSize = 9.sp,
                    color = NothingMuted
                )
                Text(
                    text = "AQI 32",
                    fontFamily = NothingDotFontFamily,
                    fontSize = 9.sp,
                    color = NothingWhite
                )
            }
        }
    }
}

/**
 * 4. Nothing Quick Settings Pods Widget
 */
@Composable
fun QuickSettingsPodsWidget() {
    var wifiOn by remember { mutableStateOf(true) }
    var bluetoothOn by remember { mutableStateOf(true) }
    var torchOn by remember { mutableStateOf(false) }
    var audioMuted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "QUICK PODS",
                fontFamily = NothingDotFontFamily,
                fontSize = 10.sp,
                color = NothingWhite
            )
            Text(
                text = "GLYPH: ${if (torchOn) "ON" else "OFF"}",
                fontFamily = NothingMonoFontFamily,
                fontSize = 8.sp,
                color = if (torchOn) NothingRed else NothingMuted
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Wi-Fi pod
            QuickPodButton(
                icon = Icons.Default.Wifi,
                isActive = wifiOn,
                label = "Wi-Fi",
                modifier = Modifier.weight(1f)
            ) { wifiOn = !wifiOn }

            // Bluetooth pod
            QuickPodButton(
                icon = Icons.Default.Bluetooth,
                isActive = bluetoothOn,
                label = "BT",
                modifier = Modifier.weight(1f)
            ) { bluetoothOn = !bluetoothOn }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Glyph torch pod
            QuickPodButton(
                icon = Icons.Default.FlashlightOn,
                isActive = torchOn,
                activeColor = NothingRed,
                label = "Glyph",
                modifier = Modifier.weight(1f)
            ) { torchOn = !torchOn }

            // Audio mute pod
            QuickPodButton(
                icon = Icons.Default.VolumeUp,
                isActive = !audioMuted,
                label = "Audio",
                modifier = Modifier.weight(1f)
            ) { audioMuted = !audioMuted }
        }
    }
}

@Composable
fun QuickPodButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    activeColor: Color = NothingWhite,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(19.dp))
            .background(if (isActive) activeColor else NothingCardElevated)
            .border(
                1.dp,
                if (isActive) activeColor else NothingBorder,
                RoundedCornerShape(19.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) NothingBlack else NothingWhite,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontFamily = NothingMonoFontFamily,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (isActive) NothingBlack else NothingMuted
            )
        }
    }
}

/**
 * 5. Media Player / Turntable Widget
 */
@Composable
fun MediaPlayerWidget() {
    var isPlaying by remember { mutableStateOf(true) }
    var trackIndex by remember { mutableIntStateOf(0) }
    val tracks = listOf("Nothing Track (1)", "Glyph Symphony (Live)", "Ear (Open) Chillout")

    val infiniteTransition = rememberInfiniteTransition(label = "vinyl_rotate")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isPlaying) 6000 else 600000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vinyl_angle"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "NOTHING X",
                fontFamily = NothingDotFontFamily,
                fontSize = 10.sp,
                color = NothingWhite
            )
            // Animated audio visualizer bars
            AudioWaveform(isPlaying = isPlaying)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Vinyl Record Disc
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(NothingBlack)
                    .border(1.dp, NothingBorder, CircleShape)
                    .rotate(if (isPlaying) rotation else 0f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val c = Offset(size.width / 2f, size.height / 2f)
                    drawCircle(color = Color(0xFF222222), radius = size.minDimension * 0.38f, center = c)
                    drawCircle(color = NothingRed, radius = size.minDimension * 0.18f, center = c)
                    drawCircle(color = NothingBlack, radius = size.minDimension * 0.08f, center = c)
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tracks[trackIndex],
                    fontFamily = NothingMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = NothingWhite,
                    maxLines = 1
                )
                Text(
                    text = "NOTHING EAR (2)",
                    fontFamily = NothingDotFontFamily,
                    fontSize = 8.sp,
                    color = NothingMuted
                )
            }
        }

        // Playback Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(NothingCardElevated)
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = NothingWhite,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(NothingCardElevated)
                    .clickable { trackIndex = (trackIndex + 1) % tracks.size },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = NothingWhite,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun AudioWaveform(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
    val b1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val b2 by infiniteTransition.animateFloat(
        initialValue = 0.8f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(550, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val b3 by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.height(14.dp)
    ) {
        val heights = if (isPlaying) listOf(b1, b2, b3, b2) else listOf(0.2f, 0.2f, 0.2f, 0.2f)
        heights.forEach { h ->
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height((h * 14).dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(NothingRed)
            )
        }
    }
}

/**
 * 6. Activity / Pedometer Ring Widget
 */
@Composable
fun ActivityStepsWidget() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACTIVITY",
                fontFamily = NothingDotFontFamily,
                fontSize = 10.sp,
                color = NothingWhite
            )
            Text(
                text = "74%",
                fontFamily = NothingDotFontFamily,
                fontSize = 9.sp,
                color = NothingRed
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Circular progress ring
            Box(
                modifier = Modifier.size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 5.dp.toPx()
                    // Track
                    drawCircle(
                        color = Color(0x22FFFFFF),
                        style = Stroke(width = stroke)
                    )
                    // Progress arc (74%)
                    drawArc(
                        color = NothingRed,
                        startAngle = -90f,
                        sweepAngle = 266f,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }
                Text(
                    text = "7.4K",
                    fontFamily = NothingDotFontFamily,
                    fontSize = 11.sp,
                    color = NothingWhite
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "7,420 STEPS",
                    fontFamily = NothingMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = NothingWhite
                )
                Text(
                    text = "340 KCAL",
                    fontFamily = NothingMonoFontFamily,
                    fontSize = 9.sp,
                    color = NothingMuted
                )
                Text(
                    text = "5.2 KM",
                    fontFamily = NothingMonoFontFamily,
                    fontSize = 9.sp,
                    color = NothingMuted
                )
            }
        }

        Text(
            text = "TARGET: 10,000",
            fontFamily = NothingMonoFontFamily,
            fontSize = 8.sp,
            color = NothingSubtle
        )
    }
}

/**
 * 7. Glyph Torch Widget
 */
@Composable
fun GlyphTorchWidget() {
    var isTorchOn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "GLYPH LIGHTS",
                fontFamily = NothingDotFontFamily,
                fontSize = 10.sp,
                color = NothingWhite
            )
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(if (isTorchOn) NothingRed else NothingSubtle)
            )
        }

        // Miniature glyph phone back preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(50.dp, 60.dp)) {
                val lit = if (isTorchOn) GlyphGlow else GlyphOff
                val strokeW = 2.5.dp.toPx()

                // Top left camera ring
                drawCircle(
                    color = lit,
                    radius = 9.dp.toPx(),
                    center = Offset(16.dp.toPx(), 18.dp.toPx()),
                    style = Stroke(width = strokeW)
                )
                // Top diagonal slash
                drawLine(
                    color = lit,
                    start = Offset(32.dp.toPx(), 8.dp.toPx()),
                    end = Offset(42.dp.toPx(), 18.dp.toPx()),
                    strokeWidth = strokeW,
                    cap = StrokeCap.Round
                )
                // Center charging coil loop
                drawCircle(
                    color = lit,
                    radius = 14.dp.toPx(),
                    center = Offset(25.dp.toPx(), 36.dp.toPx()),
                    style = Stroke(width = strokeW)
                )
                // Bottom exclamation point
                drawLine(
                    color = lit,
                    start = Offset(25.dp.toPx(), 52.dp.toPx()),
                    end = Offset(25.dp.toPx(), 58.dp.toPx()),
                    strokeWidth = strokeW,
                    cap = StrokeCap.Round
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isTorchOn) NothingWhite else NothingCardElevated)
                .clickable { isTorchOn = !isTorchOn },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isTorchOn) "TURN OFF" else "ACTIVATE",
                fontFamily = NothingMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                color = if (isTorchOn) NothingBlack else NothingWhite
            )
        }
    }
}

/**
 * 8. Voice Recorder Widget
 */
@Composable
fun VoiceRecorderWidget() {
    var isRecording by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RECORDER",
                fontFamily = NothingDotFontFamily,
                fontSize = 10.sp,
                color = NothingWhite
            )
            if (isRecording) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(NothingRed)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "REC",
                        fontFamily = NothingDotFontFamily,
                        fontSize = 8.sp,
                        color = NothingRed
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (isRecording) NothingRed else NothingCardElevated)
                    .clickable { isRecording = !isRecording },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Record",
                    tint = if (isRecording) NothingWhite else NothingWhite,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Text(
            text = if (isRecording) "RECORDING (00:14)" else "TAP TO CAPTURE",
            fontFamily = NothingMonoFontFamily,
            fontSize = 9.sp,
            color = if (isRecording) NothingWhite else NothingMuted,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

/**
 * 9. Compass Widget
 */
@Composable
fun CompassWidget() {
    var heading by remember { mutableFloatStateOf(42f) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "COMPASS",
                fontFamily = NothingDotFontFamily,
                fontSize = 10.sp,
                color = NothingWhite
            )
            Text(
                text = "${heading.toInt()}° NE",
                fontFamily = NothingDotFontFamily,
                fontSize = 10.sp,
                color = NothingRed
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = "Heading",
                tint = NothingRed,
                modifier = Modifier
                    .size(28.dp)
                    .rotate(heading)
            )
        }

        Text(
            text = "ELEVATION 24M",
            fontFamily = NothingMonoFontFamily,
            fontSize = 8.sp,
            color = NothingMuted
        )
    }
}
