package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.model.WallpaperTheme
import com.example.ui.theme.GlyphGlow
import com.example.ui.theme.NothingBlack
import com.example.ui.theme.NothingRed

@Composable
fun NothingWallpaperBackground(
    theme: WallpaperTheme,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NothingBlack)
    ) {
        when (theme) {
            WallpaperTheme.DOT_MATRIX_DARK -> {
                // Interactive grid of subtle glowing dots on pitch black with top red ambient glow
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val dotSpacing = 24.dp.toPx()
                    val dotRadius = 1.2.dp.toPx()

                    // Subtle top-center red vignette
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(NothingRed.copy(alpha = 0.08f), Color.Transparent),
                            center = Offset(size.width / 2f, size.height * 0.2f),
                            radius = size.width * 0.75f
                        )
                    )

                    val cols = (size.width / dotSpacing).toInt() + 1
                    val rows = (size.height / dotSpacing).toInt() + 1

                    for (r in 0 until rows) {
                        for (c in 0 until cols) {
                            val x = c * dotSpacing
                            val y = r * dotSpacing
                            drawCircle(
                                color = Color(0x18FFFFFF),
                                radius = dotRadius,
                                center = Offset(x, y)
                            )
                        }
                    }
                }
            }
            WallpaperTheme.GLYPH_NEON -> {
                // Subtle architectural contour lines of the Nothing Glyph interface
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 2.dp.toPx()
                    val color = Color(0x22FFFFFF)
                    val glow = GlyphGlow.copy(alpha = 0.06f)

                    // Ambient glow behind glyphs
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(glow, Color.Transparent),
                            center = Offset(size.width * 0.5f, size.height * 0.45f),
                            radius = size.width * 0.6f
                        )
                    )

                    // Camera halo contour
                    drawCircle(
                        color = color,
                        radius = size.width * 0.22f,
                        center = Offset(size.width * 0.32f, size.height * 0.28f),
                        style = Stroke(width = stroke)
                    )

                    // Top diagonal slash
                    drawLine(
                        color = color,
                        start = Offset(size.width * 0.62f, size.height * 0.14f),
                        end = Offset(size.width * 0.85f, size.height * 0.24f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )

                    // Center coil loop
                    drawCircle(
                        color = color,
                        radius = size.width * 0.32f,
                        center = Offset(size.width * 0.5f, size.height * 0.52f),
                        style = Stroke(width = stroke)
                    )

                    // Bottom exclamation bar
                    drawLine(
                        color = color,
                        start = Offset(size.width * 0.5f, size.height * 0.74f),
                        end = Offset(size.width * 0.5f, size.height * 0.82f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )
                    drawCircle(
                        color = color,
                        radius = 3.dp.toPx(),
                        center = Offset(size.width * 0.5f, size.height * 0.85f)
                    )
                }
            }
            WallpaperTheme.MATTE_OBSIDIAN -> {
                // Deepest black with a whisper of obsidian glass gradient
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0F0F0F), NothingBlack, Color(0xFF080808))
                        )
                    )
                }
            }
            WallpaperTheme.CIRCUIT_GLASS -> {
                // Translucent tech schematic with micro circuit lines
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val lineCol = Color(0x14FFFFFF)
                    val nodeCol = Color(0x28FFFFFF)
                    val w = size.width
                    val h = size.height

                    // Horizontal and vertical tech grid lines
                    for (i in 1..8) {
                        val y = h * (i / 9f)
                        drawLine(color = lineCol, start = Offset(0f, y), end = Offset(w, y), strokeWidth = 1.dp.toPx())
                    }
                    for (j in 1..5) {
                        val x = w * (j / 6f)
                        drawLine(color = lineCol, start = Offset(x, 0f), end = Offset(x, h), strokeWidth = 1.dp.toPx())
                    }

                    // Tech nodes
                    for (i in 1..8 step 2) {
                        for (j in 1..5 step 2) {
                            val cx = w * (j / 6f)
                            val cy = h * (i / 9f)
                            drawCircle(color = nodeCol, radius = 3.dp.toPx(), center = Offset(cx, cy))
                        }
                    }
                }
            }
        }
    }
}
