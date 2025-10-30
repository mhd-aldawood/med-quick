package com.example.kotlintest.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayMedium
//TODO fix this component to stop using hardcoded contentAlignment = Alignment.BottomStart
@Composable
fun NestedCirclesWithCustomRadius(circleColor: Color,circleText: String="Hello",boxModifier: Modifier= Modifier) {
    val circles = listOf(
        Triple(circleColor, 1.0f, 100f),
        Triple(circleColor, 0.6f, 130f),
        Triple(circleColor, 0.4f, 170f),
        Triple(circleColor, 0.3f, 200f),
        Triple(circleColor, 0.2f, 230f)
    )

    Box(
        modifier = boxModifier
            .size(230.dp)
            .clipToBounds(),
        contentAlignment = Alignment.BottomStart
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()//fullmaxsize
                .clipToBounds()
        ) {
            val offset = Offset(26f, size.height-50)
            circles.forEach { (color, alpha, radius) ->
                drawCircle(
                    color = color.copy(alpha = alpha),
                    radius = radius,
                    center = offset
                )
            }
        }

        // Text aligned relative to bottom start
        Text(
            text = circleText,
            color = Color.White,
            style = MaterialTheme
                .typography
                .rhDisplayBlack
                .copy(fontSize = 18.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(45.dp)
                .padding(start = 5.dp, bottom = 20.dp)
        )
    }
}
