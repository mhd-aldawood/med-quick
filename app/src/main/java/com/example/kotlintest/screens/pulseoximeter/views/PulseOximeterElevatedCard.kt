package com.example.kotlintest.screens.pulseoximeter.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.component.NestedCirclesWithCustomRadius
import com.example.kotlintest.ui.theme.LavenderGray
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplaySemiBold
import com.example.kotlintest.util.horizontalPadding


@Composable
fun PulseOximeterElevatedCard(
    cardGeneralColor: Color,
    value: String,
    unit: String = "",
    title: String,
    normalRange: String
) {
    ElevatedCard(
        modifier = Modifier
            .wrapContentWidth(),
        colors = CardDefaults
            .cardColors(
                containerColor = Color.White
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 23.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                    fontSize = 19.sp,
                    color = cardGeneralColor
                ),
                modifier = Modifier
                    .horizontalPadding(10)
                    .align(Alignment.TopCenter),
                textAlign = TextAlign.Center
            )
            Text(
                text = value,
                style = MaterialTheme.typography.rhDisplayBlack.copy(
                    fontSize = 50.sp,
                    color = cardGeneralColor
                ),
                modifier = Modifier
                    .horizontalPadding(10)
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.BottomStart),
            ) {
                NestedCirclesWithCustomRadius(circleText = unit, circleColor = cardGeneralColor)
                Column(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 10.dp, end=10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Norms", style = MaterialTheme
                        .typography
                        .rhDisplayBold
                        .copy(fontSize = 22.sp, color = LavenderGray))
                    Text(text = normalRange, style = MaterialTheme
                        .typography
                        .rhDisplayBold
                        .copy(fontSize = 22.sp, color = LavenderGray))


                }

            }
        }
    }

}
