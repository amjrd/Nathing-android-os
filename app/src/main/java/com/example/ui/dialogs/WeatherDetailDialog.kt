package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
fun WeatherDetailDialog(
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
                                text = "WEATHER",
                                fontFamily = NothingDotFontFamily,
                                fontSize = 13.sp,
                                color = NothingWhite
                            )
                        }
                        Text(
                            text = "LONDON, UK",
                            fontFamily = NothingMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = NothingMuted
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

                Spacer(modifier = Modifier.height(14.dp))

                // Hero Temperature
                Text(
                    text = "21°C",
                    fontFamily = NothingDotFontFamily,
                    fontSize = 46.sp,
                    color = NothingWhite
                )
                Text(
                    text = "PARTLY CLOUDY • FEELS LIKE 20°",
                    fontFamily = NothingMonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = NothingMuted
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Hourly Forecast Row
                Text(
                    text = "HOURLY FORECAST",
                    fontFamily = NothingDotFontFamily,
                    fontSize = 10.sp,
                    color = NothingWhite,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NothingCardElevated)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf(
                        Triple("NOW", "21°", "☀"),
                        Triple("14:00", "22°", "⛅"),
                        Triple("15:00", "24°", "⛅"),
                        Triple("16:00", "23°", "☁"),
                        Triple("17:00", "20°", "🌧")
                    ).forEach { (time, temp, _) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = time,
                                fontFamily = NothingMonoFontFamily,
                                fontSize = 9.sp,
                                color = NothingMuted
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = temp,
                                fontFamily = NothingDotFontFamily,
                                fontSize = 12.sp,
                                color = NothingWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2x2 Metric Pods
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WeatherMetricCard(
                        title = "WIND",
                        value = "18 km/h",
                        detail = "ENE GUSTS 24",
                        icon = Icons.Default.Air,
                        modifier = Modifier.weight(1f)
                    )
                    WeatherMetricCard(
                        title = "HUMIDITY",
                        value = "64%",
                        detail = "DEW POINT 12°",
                        icon = Icons.Default.WaterDrop,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WeatherMetricCard(
                        title = "UV INDEX",
                        value = "3 MODERATE",
                        detail = "PEAK 13:00",
                        icon = Icons.Default.Grain,
                        modifier = Modifier.weight(1f)
                    )
                    WeatherMetricCard(
                        title = "PRESSURE",
                        value = "1016 hPa",
                        detail = "STEADY",
                        icon = Icons.Default.Compress,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherMetricCard(
    title: String,
    value: String,
    detail: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(NothingCardElevated)
            .border(1.dp, NothingBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontFamily = NothingDotFontFamily,
                    fontSize = 9.sp,
                    color = NothingMuted
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = NothingWhite,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontFamily = NothingDotFontFamily,
                fontSize = 13.sp,
                color = NothingWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = detail,
                fontFamily = NothingMonoFontFamily,
                fontSize = 8.sp,
                color = NothingMuted
            )
        }
    }
}
