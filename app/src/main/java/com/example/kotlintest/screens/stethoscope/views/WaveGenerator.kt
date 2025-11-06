package com.example.kotlintest.screens.stethoscope.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kotlintest.ui.theme.Periwinkle
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun AudioWaveform(
    samples: ShortArray,
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFFBFCDE5),        // light bluish like your mock
    dotColor: Color = Color(0xFFBFCDE5),
    barWidth: Dp = 4.dp,
    barGap: Dp = 6.dp,
    minBarHeight: Dp = 6.dp,                    // tiny bars still visible
    verticalPadding: Dp = 8.dp,
    showCenterDots: Boolean = false,
    dotRadius: Dp = 2.dp,
    dotSpacing: Dp = 12.dp,                     // distance between center dots
    amplitudeScale: Float = 1f                  // tweak if your PCM is quiet/loud
) {
    val density = LocalDensity.current
    val barW = with(density) { barWidth.toPx() }
    val gapW = with(density) { barGap.toPx() }
    val minH = with(density) { minBarHeight.toPx() }
    val vPad = with(density) { verticalPadding.toPx() }
    val dotR = with(density) { dotRadius.toPx() }
    val dotStep = with(density) { dotSpacing.toPx() }

    // Precompute downsampled amplitudes -> one value per bar we can draw
    val amplitudes = remember(samples) {
        if (samples.isEmpty()) emptyList()
        else {
            // absolute amplitudes
            val abs = samples.map { kotlin.math.abs(it.toInt()) }
            // number of bars that fit once we know canvas width (we'll trim later in draw)
            abs
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp) // set the overall height you want
    ) {
        val w = size.width
        val h = size.height
        val centerY = h / 2f
        val contentTop = vPad
        val contentBottom = h - vPad
        val maxDrawableHeight = (contentBottom - contentTop).coerceAtLeast(1f)

        // How many bars can we fit across?
        val barsFit = if (barW + gapW <= 0f) 0 else (w / (barW + gapW)).toInt().coerceAtLeast(1)

        // If we have no data, just draw a dotted center line and leave.
        if (amplitudes.isEmpty() || barsFit <= 0) {
            if (showCenterDots) {
                var x = dotStep / 2f
                while (x < w) {
                    drawCircle(dotColor, radius = dotR, center = Offset(x, centerY))
                    x += dotStep
                }
            }
            return@Canvas
        }

        // Downsample PCM into 'barsFit' buckets (use RMS for a nicer look)
        val perBar = (amplitudes.size / barsFit).coerceAtLeast(1)
        val barValues = IntArray(barsFit) { barIdx ->
            val start = barIdx * perBar
            val endExclusive = kotlin.math.min(start + perBar, amplitudes.size)
            var sumSq = 0L
            var count = 0
            for (i in start until endExclusive) {
                val v = amplitudes[i]
                sumSq += (v * v).toLong()
                count++
            }
            if (count == 0) 0 else kotlin.math.sqrt(sumSq.toDouble() / count).toInt()
        }

        // Normalize to 0..1 using the max bar value (don’t explode on silence)
        val maxVal = (barValues.maxOrNull() ?: 1).coerceAtLeast(1)
        val xStart = 0f

        // Dotted center guide
        if (showCenterDots) {
            var x = dotStep / 2f
            while (x < w) {
                drawCircle(dotColor.copy(alpha = 0.9f), radius = dotR, center = Offset(x, centerY))
                x += dotStep
            }
        }

        // Draw bars
        var x = xStart
        for (i in 0 until barsFit) {
            val norm = (barValues[i].toFloat() / maxVal) * amplitudeScale
            val barH = (norm * maxDrawableHeight).coerceAtLeast(minH)
            val top = centerY - barH / 2f
            val left = x
            val right = (x + barW).coerceAtMost(w)
            val rect = Rect(left, top, right, top + barH)
            drawRoundRect(
                color = barColor,
                topLeft = rect.topLeft,
                size = rect.size,
                cornerRadius = CornerRadius(barW / 2f, barW / 2f) // nice pill bars
            )
            x += barW + gapW
            if (x > w) break
        }
    }
}

@Preview
@Composable
fun WavePreview() {
    val samples = remember {
        ShortArray(2048) { i ->
            val freq = 0.05f
            val amp = (sin(i * freq) * Short.MAX_VALUE * 0.5).toInt().toShort()
            (amp * (0.5 + 0.5 * sin(i * 0.001))).toInt().toShort()
        }
    }
    AudioWaveform(
        samples = samples,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        barWidth = 4.dp,
        barGap = 6.dp
    )
}

@Composable
fun AudioWaveformStreaming(
    latestValue: Float?,                 // push one value each recomposition; null = no update
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFFBFCDE5),
    barWidth: Dp = 4.dp,
    barGap: Dp = 6.dp,
    minBarHeight: Dp = 6.dp,
    verticalPadding: Dp = 8.dp,
    normalizeTo: Float? = null,         // if set (e.g. 32767f), fixes scaling; else auto-normalize
    height: Dp = 90.dp
) {
    val density = LocalDensity.current
    val bw = with(density) { barWidth.toPx() }
    val gap = with(density) { barGap.toPx() }
    val minH = with(density) { minBarHeight.toPx() }
    val vPad = with(density) { verticalPadding.toPx() }

    // Rolling buffer of bar amplitudes (most recent at the end)
    val bars = remember { mutableStateListOf<Float>() }

    // How many bars fit across the canvas; updated on size changes
    var capacity by remember { mutableStateOf(100) }

    Box(
        modifier = modifier
            .height(height)
            .onSizeChanged { sz ->
                val fit = ((sz.width) / (bw + gap)).toInt().coerceAtLeast(1)
                capacity = fit
                // trim if our list is already longer than what fits
                while (bars.size > capacity) bars.removeAt(0)
            }
    ) {
        // Append the newest value (if any) and drop the oldest when exceeding capacity
        LaunchedEffect(latestValue, capacity) {
            latestValue?.let { v ->
                bars += kotlin.math.abs(v)
                if (bars.size > capacity) bars.removeAt(0)
            }
        }

        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val centerY = h / 2f
            val maxDraw = (h - 2f * vPad).coerceAtLeast(1f)

            if (bars.isEmpty()) return@Canvas

            // Scale: fixed (normalizeTo) or dynamic (current max)
            val denom = when {
                normalizeTo != null && normalizeTo > 0f -> normalizeTo
                else -> (bars.maxOrNull() ?: 1f).coerceAtLeast(1f)
            }

            var x = w
            for (i in bars.lastIndex downTo 0) {
                x -= bw
                if (x < 0f) break

                val ratio = (bars[i] / denom).coerceIn(0f, 1f)
                val barH = (ratio * maxDraw).coerceAtLeast(minH)
                val top = centerY - barH / 2f

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x, top),
                    size = Size(bw, barH),
                    cornerRadius = CornerRadius(bw / 2f, bw / 2f)
                )

                x -= gap
                if (x <= 0f) break
            }
        }
    }
}

@Preview
@Composable
fun WaveHeart() {
    var tick by remember { mutableStateOf(0f) }

//    // Fake stream: emit one value ~60 times/sec
//    LaunchedEffect(Unit) {
//        while (true) {
//            tick += 0.2f
//            delay(100)
//        }
//    }
//
//    // Compute a value to feed; replace with your real amplitude
//    val value = remember(tick) {
//        val base = kotlin.math.sin(tick) * 0.6f + 0.4f
//        (base * 32767f) // pretend PCM amplitude range
//    }
    // Simulate device sending new heart rate readings
    LaunchedEffect(Unit) {
        while (true) {
            tick = (60..120).random().toFloat()  // fake BPM values
            delay(200) // adjust speed of updates (e.g. device refresh rate)
        }
    }

    AudioWaveformStreaming(
        latestValue = tick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        normalizeTo = 180f, // max expected BPM
        barColor = Periwinkle,
        minBarHeight = 10.dp
    )
//    AudioWaveformStreaming(
//        latestValue = value,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        barWidth = 4.dp,
//        barGap = 6.dp,
//        minBarHeight = 6.dp,
//        normalizeTo = null,   // set known max to keep stable scale; or null for auto
//        height = 90.dp
//    )
}
