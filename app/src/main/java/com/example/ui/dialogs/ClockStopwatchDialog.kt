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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun ClockStopwatchDialog(
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Stopwatch, 1: World Clock
    var isRunning by remember { mutableStateOf(false) }
    var elapsedMillis by remember { mutableLongStateOf(0L) }
    var laps by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(isRunning) {
        val start = System.currentTimeMillis() - elapsedMillis
        while (isRunning) {
            elapsedMillis = System.currentTimeMillis() - start
            delay(33) // ~30 fps update
        }
    }

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
                    .padding(20.dp),
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
                            text = if (selectedTab == 0) "STOPWATCH" else "WORLD CLOCK",
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

                Spacer(modifier = Modifier.height(14.dp))

                // Tab Switcher: Stopwatch vs World Clock
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(NothingCardElevated)
                        .padding(4.dp)
                ) {
                    listOf("STOPWATCH", "WORLD CLOCK").forEachIndexed { i, title ->
                        val isSel = selectedTab == i
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) NothingWhite else Color.Transparent)
                                .clickable { selectedTab = i }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = title,
                                fontFamily = NothingMonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = if (isSel) NothingBlack else NothingMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (selectedTab == 0) {
                    // Stopwatch Tab
                    val totalSecs = elapsedMillis / 1000
                    val mm = String.format(Locale.getDefault(), "%02d", totalSecs / 60)
                    val ss = String.format(Locale.getDefault(), "%02d", totalSecs % 60)
                    val ms = String.format(Locale.getDefault(), "%02d", (elapsedMillis % 1000) / 10)

                    Text(
                        text = "$mm:$ss.$ms",
                        fontFamily = NothingDotFontFamily,
                        fontSize = 32.sp,
                        color = NothingWhite
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Buttons: Lap/Reset, Play/Pause
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Lap / Reset
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(NothingCardElevated)
                                .clickable {
                                    if (isRunning) {
                                        laps = listOf("$mm:$ss.$ms") + laps
                                    } else {
                                        elapsedMillis = 0L
                                        laps = emptyList()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isRunning) Icons.Default.Flag else Icons.Default.Refresh,
                                contentDescription = if (isRunning) "Lap" else "Reset",
                                tint = NothingWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Play / Pause (Large Red/White button)
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(if (isRunning) NothingWhite else NothingRed)
                                .clickable { isRunning = !isRunning },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = if (isRunning) NothingBlack else NothingWhite,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Laps List
                    if (laps.isNotEmpty()) {
                        Text(
                            text = "LAPS",
                            fontFamily = NothingDotFontFamily,
                            fontSize = 10.sp,
                            color = NothingMuted,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            itemsIndexed(laps) { idx, lapTime ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "LAP ${laps.size - idx}",
                                        fontFamily = NothingMonoFontFamily,
                                        fontSize = 11.sp,
                                        color = NothingMuted
                                    )
                                    Text(
                                        text = lapTime,
                                        fontFamily = NothingDotFontFamily,
                                        fontSize = 12.sp,
                                        color = NothingWhite
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // World Clock Tab
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf(
                            Triple("LONDON", "+0 HRS", "13:42"),
                            Triple("NEW YORK", "-5 HRS", "08:42"),
                            Triple("TOKYO", "+9 HRS", "22:42"),
                            Triple("STOCKHOLM", "+1 HR", "14:42")
                        ).forEach { (city, offset, time) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(NothingCardElevated)
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = city,
                                        fontFamily = NothingDotFontFamily,
                                        fontSize = 12.sp,
                                        color = NothingWhite
                                    )
                                    Text(
                                        text = offset,
                                        fontFamily = NothingMonoFontFamily,
                                        fontSize = 9.sp,
                                        color = NothingMuted
                                    )
                                }
                                Text(
                                    text = time,
                                    fontFamily = NothingDotFontFamily,
                                    fontSize = 18.sp,
                                    color = NothingWhite
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
