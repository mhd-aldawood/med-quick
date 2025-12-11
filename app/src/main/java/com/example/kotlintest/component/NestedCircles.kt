package com.example.kotlintest.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.rhDisplayBlack

//TODO fix this component to stop using hardcoded contentAlignment = Alignment.BottomStart
@Composable
fun NestedCirclesWithCustomRadius(
    circleColor: Color,
    circleText: String = "Hello",
    unitFontSize:Int=18,
    boxModifier: Modifier = Modifier
) {
    var textWidthPx by remember { mutableFloatStateOf(0f) }

    // Differences exactly like your original radii
    val diffs = listOf(0f, 30f, 70f, 100f, 130f)

    // Generate radii based on text width
    val circles = diffs.mapIndexed { index, diff ->
        Triple(
            circleColor,
            when (index) {
                0 -> 1.0f
                1 -> 0.6f
                2 -> 0.4f
                3 -> 0.3f
                else -> 0.2f
            },
            textWidthPx + diff   // 🔥 text width + difference
        )
    }

    Box(
        modifier = boxModifier
            .size(230.dp)
            .clipToBounds(),
        contentAlignment = Alignment.BottomStart
    ) {

        // Draw circles only after the text width is measured
        if (textWidthPx > 0f) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .clipToBounds()
            ) {
                val offset = Offset(26f, size.height - 50)

                circles.forEach { (color, alpha, radius) ->
                    drawCircle(
                        color = color.copy(alpha = alpha),
                        radius = radius,
                        center = offset
                    )
                }
            }
        }

        // Text
        Text(
            text = circleText,
            color = Color.White,
            style = MaterialTheme.typography.rhDisplayBlack.copy(
                fontSize = unitFontSize.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 5.dp, bottom = 20.dp)
                .onGloballyPositioned { coords ->
                    textWidthPx = coords.size.width.toFloat()
                }
        )
    }
}
