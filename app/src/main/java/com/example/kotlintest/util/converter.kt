package com.example.kotlintest.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Converts XD font size (px) to Android sp.
 *
 * @param xdFontSize Font size in XD pixels.
 * @param designWidthPx The XD artboard width (optional). Default auto-detects 720 or 1080.
 */
@Composable
fun xdFontToSp(xdFontSize: Float, designWidthPx: Int? = null): TextUnit {
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp
    val designWidth = designWidthPx ?: when {
        screenWidthDp <= 400 -> 720
        else -> 1080
    }

    val spPerXdPixel = screenWidthDp / designWidth.toFloat()
    return (xdFontSize * spPerXdPixel).sp
}
/**
 * Converts XD pixel values to Android dp.
 *
 * @param xdPx Value in XD pixels.
 * @param designWidthPx The XD artboard width (optional). Default auto-detects 720 or 1080.
 */
@Composable
fun xdPxToDp(xdPx: Int, designWidthPx: Int? = null): Dp {
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp
    val designWidth = designWidthPx ?: when {
        screenWidthDp <= 400 -> 720   // Smaller devices → assume 2x design
        else -> 1080                  // Modern devices → assume 3x design
    }

    val dpPerXdPixel = screenWidthDp / designWidth.toFloat()
    return (xdPx * dpPerXdPixel).dp
}
/**
 * Converts raw pixel values to dp within a Compose context.
 */
@Composable
fun pixelsToDp(pixels: Float): Dp {
    val density = LocalDensity.current
    return with(density) { pixels.toDp() }
}
//these two method works fine
// px(Float) → dp(Dp)
@Composable
 fun pxToDp(value:Float): Dp {
    return (value / LocalDensity.current.density).dp
}

// px(Float) → sp(TextUnit)
@Composable
 fun pxToSp(value:Float): TextUnit {
    return  (value / LocalDensity.current.fontScale).sp
}

private const val DESIGN_WIDTH_PX = 2880f

@Composable
fun rememberScaleFactor(): Float {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    val screenWidthDp = configuration.screenWidthDp.toFloat()
    val screenWidthPx = screenWidthDp * density
    return remember(screenWidthPx) { screenWidthPx / DESIGN_WIDTH_PX }
}

/**
 * Converts a pixel value from XD/Figma to a scaled Dp value
 * based on current screen width relative to design width.
 */
@Composable
fun scalePxToDp(px: Float): Dp {
    val density = LocalDensity.current.density
    val scale = rememberScaleFactor()
    return (px / density * scale).dp
}
@Composable
fun scaleSp(spValue: Float): TextUnit {
    val scale = rememberScaleFactor()
    return (spValue * scale).sp
}

fun celsiusToFahrenheit(celsius: Float): Float {
    return celsius * 9 / 5 + 32
}