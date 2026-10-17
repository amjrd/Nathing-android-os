package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NothingBlack
import com.example.ui.theme.NothingMuted
import com.example.ui.theme.NothingRed
import com.example.ui.theme.NothingWhite

/**
 * 5x7 Dot-Matrix font representation for iconic Nothing OS digits and symbols.
 */
object DotMatrixFont {
    // 5x7 grid: each number is represented by 7 rows of 5 columns (0=off, 1=on)
    private val DIGITS = mapOf(
        '0' to arrayOf(
            "01110",
            "10001",
            "10011",
            "10101",
            "11001",
            "10001",
            "01110"
        ),
        '1' to arrayOf(
            "00100",
            "01100",
            "00100",
            "00100",
            "00100",
            "00100",
            "01110"
        ),
        '2' to arrayOf(
            "01110",
            "10001",
            "00001",
            "00010",
            "00100",
            "01000",
            "11111"
        ),
        '3' to arrayOf(
            "01110",
            "10001",
            "00001",
            "00110",
            "00001",
            "10001",
            "01110"
        ),
        '4' to arrayOf(
            "00010",
            "00110",
            "01010",
            "10010",
            "11111",
            "00010",
            "00010"
        ),
        '5' to arrayOf(
            "11111",
            "10000",
            "11110",
            "00001",
            "00001",
            "10001",
            "01110"
        ),
        '6' to arrayOf(
            "00110",
            "01000",
            "10000",
            "11110",
            "10001",
            "10001",
            "01110"
        ),
        '7' to arrayOf(
            "11111",
            "00001",
            "00010",
            "00100",
            "01000",
            "01000",
            "01000"
        ),
        '8' to arrayOf(
            "01110",
            "10001",
            "10001",
            "01110",
            "10001",
            "10001",
            "01110"
        ),
        '9' to arrayOf(
            "01110",
            "10001",
            "10001",
            "01111",
            "00001",
            "00010",
            "01100"
        ),
        ':' to arrayOf(
            "00000",
            "00100",
            "00100",
            "00000",
            "00100",
            "00100",
            "00000"
        ),
        '%' to arrayOf(
            "11001",
            "11010",
            "00100",
            "01000",
            "01011",
            "10011",
            "00000"
        ),
        '°' to arrayOf(
            "01100",
            "10010",
            "10010",
            "01100",
            "00000",
            "00000",
            "00000"
        ),
        ' ' to arrayOf(
            "00000",
            "00000",
            "00000",
            "00000",
            "00000",
            "00000",
            "00000"
        )
    )

    fun getGrid(char: Char): Array<String> = DIGITS[char] ?: DIGITS[' ']!!
}

@Composable
fun DotMatrixChar(
    char: Char,
    dotSize: Dp = 3.dp,
    dotSpacing: Dp = 1.5.dp,
    activeColor: Color = NothingWhite,
    inactiveColor: Color = Color(0x18FFFFFF),
    modifier: Modifier = Modifier
) {
    val grid = DotMatrixFont.getGrid(char)
    val cols = 5
    val rows = 7

    val width = (dotSize * cols) + (dotSpacing * (cols - 1))
    val height = (dotSize * rows) + (dotSpacing * (rows - 1))

    Canvas(modifier = modifier.size(width, height)) {
        val pxDotSize = dotSize.toPx()
        val pxSpacing = dotSpacing.toPx()
        val radius = pxDotSize / 2f

        for (row in 0 until rows) {
            val rowStr = grid.getOrElse(row) { "00000" }
            for (col in 0 until cols) {
                val isActive = rowStr.getOrElse(col) { '0' } == '1'
                val cx = col * (pxDotSize + pxSpacing) + radius
                val cy = row * (pxDotSize + pxSpacing) + radius

                drawCircle(
                    color = if (isActive) activeColor else inactiveColor,
                    radius = radius,
                    center = Offset(cx, cy)
                )
            }
        }
    }
}

@Composable
fun DotMatrixString(
    text: String,
    modifier: Modifier = Modifier,
    dotSize: Dp = 3.dp,
    dotSpacing: Dp = 1.5.dp,
    charSpacing: Dp = 4.dp,
    activeColor: Color = NothingWhite,
    highlightColor: Color = NothingRed,
    highlightChars: Set<Char> = setOf(':')
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        text.forEachIndexed { index, char ->
            val color = if (char in highlightChars) highlightColor else activeColor
            DotMatrixChar(
                char = char,
                dotSize = dotSize,
                dotSpacing = dotSpacing,
                activeColor = color
            )
            if (index < text.length - 1) {
                Box(modifier = Modifier.size(charSpacing, 1.dp))
            }
        }
    }
}
