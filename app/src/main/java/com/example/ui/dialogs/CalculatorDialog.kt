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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun CalculatorDialog(
    onDismiss: () -> Unit
) {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableDoubleStateOf(0.0) }
    var pendingOp by remember { mutableStateOf<String?>(null) }
    var shouldClearOnNextDigit by remember { mutableStateOf(false) }

    fun onDigit(d: String) {
        if (display == "0" || shouldClearOnNextDigit) {
            display = d
            shouldClearOnNextDigit = false
        } else {
            if (display.length < 9) {
                display += d
            }
        }
    }

    fun onOp(op: String) {
        operand1 = display.toDoubleOrNull() ?: 0.0
        pendingOp = op
        shouldClearOnNextDigit = true
    }

    fun onEquals() {
        val op = pendingOp ?: return
        val operand2 = display.toDoubleOrNull() ?: 0.0
        val result = when (op) {
            "+" -> operand1 + operand2
            "-" -> operand1 - operand2
            "×" -> operand1 * operand2
            "÷" -> if (operand2 != 0.0) operand1 / operand2 else 0.0
            else -> operand2
        }
        display = if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            String.format(java.util.Locale.getDefault(), "%.3f", result).trimEnd('0').trimEnd('.')
        }
        pendingOp = null
        shouldClearOnNextDigit = true
    }

    fun onClear() {
        display = "0"
        operand1 = 0.0
        pendingOp = null
        shouldClearOnNextDigit = false
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
                            text = "CALCULATOR",
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

                Spacer(modifier = Modifier.height(18.dp))

                // Display Window in N-Dot
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NothingBlack)
                        .border(1.dp, NothingBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        if (pendingOp != null) {
                            Text(
                                text = "${operand1.toLong()} $pendingOp",
                                fontFamily = NothingMonoFontFamily,
                                fontSize = 11.sp,
                                color = NothingRed
                            )
                        }
                        Text(
                            text = display,
                            fontFamily = NothingDotFontFamily,
                            fontSize = 28.sp,
                            color = NothingWhite,
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Keypad Grid: 4x5
                val buttons = listOf(
                    listOf("C", "+/-", "%", "÷"),
                    listOf("7", "8", "9", "×"),
                    listOf("4", "5", "6", "-"),
                    listOf("1", "2", "3", "+"),
                    listOf("0", "00", ".", "=")
                )

                buttons.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { label ->
                            val isOp = label in listOf("÷", "×", "-", "+", "=")
                            val isAction = label in listOf("C", "+/-", "%")

                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isOp -> NothingRed
                                            isAction -> NothingCardElevated
                                            else -> Color(0xFF1E1E1E)
                                        }
                                    )
                                    .clickable {
                                        when (label) {
                                            "C" -> onClear()
                                            "+/-" -> {
                                                display = if (display.startsWith("-")) display.drop(1) else "-$display"
                                            }
                                            "%" -> {
                                                val v = display.toDoubleOrNull() ?: 0.0
                                                display = (v / 100.0).toString()
                                            }
                                            "÷", "×", "-", "+" -> onOp(label)
                                            "=" -> onEquals()
                                            else -> onDigit(label)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontFamily = if (isOp) NothingMonoFontFamily else NothingDotFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = if (isOp) 18.sp else 16.sp,
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
