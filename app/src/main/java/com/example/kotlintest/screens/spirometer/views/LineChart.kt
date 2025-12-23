package com.example.kotlintest.screens.spirometer.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.kotlintest.screens.spirometer.models.WavePoint
@Composable
fun LineChartWithAxis(
    modifier: Modifier = Modifier,
    xValues: List<Float>,
    yValues: List<Float>,
    xMin: Float,
    xMax: Float,
    xStep: Float,
    yMin: Float,
    yMax: Float,
    yStep: Float,
    xUnit: String,
    yUnit: String
) {
    Canvas(modifier = modifier) {

        val chartPadding = 60f
        val width = size.width - chartPadding
        val height = size.height - chartPadding

        // --- Draw axes ---
        drawLine(
            Color.Black,
            start = androidx.compose.ui.geometry.Offset(chartPadding, height),
            end = androidx.compose.ui.geometry.Offset(chartPadding + width, height),
            strokeWidth = 2f
        )

        drawLine(
            Color.Black,
            start = androidx.compose.ui.geometry.Offset(chartPadding, 0f),
            end = androidx.compose.ui.geometry.Offset(chartPadding, height),
            strokeWidth = 2f
        )

        // --- X axis ticks ---
        var xTick = xMin
        while (xTick <= xMax) {
            val x = chartPadding + ((xTick - xMin) / (xMax - xMin)) * width

            drawLine(
                Color.Black,
                start = androidx.compose.ui.geometry.Offset(x, height),
                end = androidx.compose.ui.geometry.Offset(x, height + 10f),
                strokeWidth = 1f
            )

            drawContext.canvas.nativeCanvas.drawText(
                "${xTick.toInt()}$xUnit",
                x - 10,
                height + 30,
                android.graphics.Paint().apply { textSize = 24f }
            )

            xTick += xStep
        }

        // --- Y axis ticks ---
        var yTick = yMin
        while (yTick <= yMax) {
            val y = height - ((yTick - yMin) / (yMax - yMin)) * height

            drawLine(
                Color.Black,
                start = androidx.compose.ui.geometry.Offset(chartPadding - 10f, y),
                end = androidx.compose.ui.geometry.Offset(chartPadding, y),
                strokeWidth = 1f
            )

            drawContext.canvas.nativeCanvas.drawText(
                "${yTick.toInt()}$yUnit",
                0f,
                y + 5,
                android.graphics.Paint().apply { textSize = 24f }
            )

            yTick += yStep
        }

        // --- Draw data path ---
        val path = Path()

        xValues.indices.forEach { i ->
            val x = chartPadding + ((xValues[i] - xMin) / (xMax - xMin)) * width
            val y = height - ((yValues[i] - yMin) / (yMax - yMin)) * height

            if (i == 0) path.moveTo(x, y)
            else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 3f)
        )
    }
}
@Composable
fun VolumeTimeChart(wavePoints: List<WavePoint>) {
    LineChartWithAxis(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        xValues = wavePoints.map { it.time },
        yValues = wavePoints.map { it.volume },
        xMin = 0f,
        xMax = 6f,
        xStep = 1f,
        yMin = 0f,
        yMax = 6f,
        yStep = 1f,
        xUnit = "s",
        yUnit = "L"
    )
}
@Composable
fun FlowVolumeChart(wavePoints: List<WavePoint>) {
    LineChartWithAxis(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        xValues = wavePoints.map { it.volume },
        yValues = wavePoints.map { it.flow },
        xMin = 0f,
        xMax = 6f,
        xStep = 1f,
        yMin = 2f,
        yMax = 12f,
        yStep = 2f,
        xUnit = "L",
        yUnit = "L/s"
    )
}
