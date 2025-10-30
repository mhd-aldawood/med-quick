package com.example.kotlintest.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlintest.ui.theme.ChineseYellow

@Composable
fun SimpleThermometer(
    value: Float,
    secondValue: Float? = null,
    modifier: Modifier = Modifier,
    minValue: Float = 15f,
    maxValue: Float = 55f,
    colorIndicator: Color = ChineseYellow,
    incrementNumber: Int = 5,
    numberOfDashes: Int = 5,
    gap:Float = -40f//represent the distance between number and dashes
) {
    val animatedValue = animateFloatAsState(targetValue = value.coerceIn(minValue, maxValue)).value
    var animatedSecondValue: Float? = null
    secondValue?.let {
        animatedSecondValue =
            animateFloatAsState(targetValue = it.coerceIn(minValue, maxValue)).value
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val right = width * 0.7f
        val left = width * 0.3f

        val range = maxValue - minValue
        val stepCount = ((range / incrementNumber).toInt()) * numberOfDashes
        val tickSpacing = height / stepCount

        val majorTickWidth = width * 0.25f
        val minorTickWidth = width * 0.15f

        var currentValue = minValue
        for (i in 0..stepCount) {
            val y = height - i * tickSpacing
            val isMajor = i % numberOfDashes == 0
            val tickWidth = if (isMajor) majorTickWidth else minorTickWidth

            drawLine(
                color = Color.LightGray,
                start = Offset(right - tickWidth, y),
                end = Offset(right, y),
                strokeWidth = if (isMajor) 2f else 1f
            )

            if (isMajor) {
                drawContext.canvas.nativeCanvas.drawText(
                    currentValue.toInt().toString(),
                    left+gap,
                    y + 6f,
                    android.graphics.Paint().apply {
                        color = Color.Gray.toArgb()
                        textSize = 30f
                        isAntiAlias = true
                        textAlign = android.graphics.Paint.Align.LEFT
                    }
                )
                currentValue += incrementNumber
            }
        }

        val indicatorY = height - (animatedValue - minValue) / range * height
        drawLine(
            color = colorIndicator,
            start = Offset(right - majorTickWidth, indicatorY),
            end = Offset(right, indicatorY),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        animatedSecondValue?.let {
            val indicatorYSecond = height - (it - minValue) / range * height
            drawLine(
                color = colorIndicator,
                start = Offset(right - majorTickWidth, indicatorYSecond),
                end = Offset(right, indicatorYSecond),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Preview
@Composable
private fun SimpleThermometerExample() {
    var temp by remember { mutableStateOf(33f) }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        SimpleThermometer(
            value = temp,
            secondValue = 20F,
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .align(Alignment.Center),
            minValue = 15f,
            maxValue = 55f,
            incrementNumber = 5
        )

    }
}

