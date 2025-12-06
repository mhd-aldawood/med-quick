package com.example.kotlintest.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

//
///**
// * Converts Adobe XD pixel values to dp based on your design scale.
// *
// * @param designScale 1f for 1× (mdpi), 2f for 2× (xhdpi), 3f for 3× (xxhdpi), etc.
// */
//@Composable
//fun Int.toDpFromXd(designScale: Float = 2f): Dp {
//    val density = LocalDensity.current
//    val pxValue = this / designScale
//    return with(density) { pxValue.dp }
//}


/**
 * Automatically converts XD pixel values to Android dp.
 *
 * @param designWidthPx The width of your XD artboard in pixels (default auto-detects 720 or 1080).
 */
@Composable
fun Int.toDpFromXd(designWidthPx: Int? = null): Dp {
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp
    val designWidth = designWidthPx ?: when {
        screenWidthDp <= 400 -> 720   // Smaller devices → assume 2x design
        else -> 1080                   // Modern devices → assume 3x design
    }

    val dpPerXdPixel = screenWidthDp / designWidth.toFloat()
    return (this * dpPerXdPixel).dp
}

@Composable
fun Modifier.verticalPadding(value: Int): Modifier = this.padding(vertical = value.dp)

@Composable
fun Modifier.horizontalPadding(value: Int): Modifier = this.padding(horizontal = value.dp)

fun Modifier.throttledClickable(
    throttleDurationMillis: Long = 500L,
    onClick: () -> Unit
): Modifier = composed {
    val scope = rememberCoroutineScope()
    val clickFlow = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

    remember(onClick) {
        clickFlow
            .debounce(throttleDurationMillis)
            .onEach { onClick() }
            .launchIn(scope)
    }

    this.clickable {
        clickFlow.tryEmit(Unit)
    }
}