package com.example.kotlintest.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.LavenderGray
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplaySemiBold
import com.example.kotlintest.util.horizontalPadding

@Composable
fun NormalRangeCard(
    cardGeneralColor: Color,
    value: String,
    valueTextColor: Color = cardGeneralColor,
    unit: String = "",
    cardUnitColor: Color = cardGeneralColor,
    title: String,
    normalRange: String,
    boxModifier: Modifier = Modifier,
    valueFontSize: Int = 50,
    normalRangeFontSize: Int = 22,
    unitFontSize:Int=18,
    valueTextAlignment: Alignment = Alignment.Center
) {
    CardWithShadowOnBorder(
        modifier = Modifier
            .wrapContentWidth(),
        cardContainerColor = Lotion
    ) {
        Box(
            modifier = boxModifier
                .padding(top = 10.dp)
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
                    fontSize = valueFontSize.sp,
                    color = valueTextColor
                ),
                modifier = Modifier
                    .horizontalPadding(10)
                    .align(valueTextAlignment),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
            )
            {
                NestedCirclesWithCustomRadius(circleText = unit, circleColor = cardGeneralColor, unitFontSize = unitFontSize)
                Text(
                    text = normalRange,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 10.dp, end = 10.dp), style = MaterialTheme
                        .typography
                        .rhDisplayBold
                        .copy(fontSize = normalRangeFontSize.sp, color = LavenderGray)
                )
            }
        }
    }

}