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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.HomeWidgetType
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
fun WidgetPickerDialog(
    onSelectWidget: (HomeWidgetType, Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val widgetOptions = listOf(
        Triple(HomeWidgetType.DOT_CLOCK, "N-DOT CLOCK (4x2)", "Iconic digital clock with day, date, and weather status"),
        Triple(HomeWidgetType.ANALOG_CLOCK, "ANALOG CLOCK (2x2)", "Minimalist circular dial with dot indices and red second hand"),
        Triple(HomeWidgetType.WEATHER_METRIC, "WEATHER FORECAST (2x2)", "Live temperature in dot-matrix, condition, AQI, and high/low"),
        Triple(HomeWidgetType.QUICK_SETTINGS, "QUICK SETTINGS PODS (2x2)", "Large pill buttons for Wi-Fi, Bluetooth, Glyph, and Sound"),
        Triple(HomeWidgetType.MEDIA_PLAYER, "NOTHING AUDIO PLAYER (2x2)", "Rotating vinyl disc, live waveform bars, and skip controls"),
        Triple(HomeWidgetType.STEP_COUNTER, "ACTIVITY RING (2x2)", "Pedometer circular progress ring with steps, kcal, and distance"),
        Triple(HomeWidgetType.GLYPH_TORCH, "GLYPH LIGHTS (2x2)", "Miniature Nothing Phone back panel with glyph flashlight toggle"),
        Triple(HomeWidgetType.VOICE_RECORDER, "TAPE RECORDER (2x2)", "Cassette spool animation with one-tap voice recording"),
        Triple(HomeWidgetType.COMPASS, "COMPASS DIAL (2x2)", "Minimalist heading with elevation and degrees in dot-matrix")
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
                    .height(520.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(NothingCardBg)
                    .border(1.dp, NothingBorder, RoundedCornerShape(28.dp))
                    .padding(20.dp)
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
                            text = "NOTHING WIDGETS",
                            fontFamily = NothingDotFontFamily,
                            fontSize = 13.sp,
                            color = NothingWhite
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = NothingWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(widgetOptions) { (type, title, desc) ->
                        val colSpan = if (type == HomeWidgetType.DOT_CLOCK) 4 else 2
                        val rowSpan = 2

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(NothingCardElevated)
                                .border(1.dp, NothingBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    onSelectWidget(type, colSpan, rowSpan)
                                    onDismiss()
                                }
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = title,
                                    fontFamily = NothingDotFontFamily,
                                    fontSize = 11.sp,
                                    color = NothingWhite
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = desc,
                                    fontFamily = NothingMonoFontFamily,
                                    fontSize = 9.sp,
                                    color = NothingMuted
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(NothingBlack)
                                    .border(1.dp, NothingBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = NothingWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
