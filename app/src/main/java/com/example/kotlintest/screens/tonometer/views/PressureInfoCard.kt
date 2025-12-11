package com.example.kotlintest.screens.tonometer.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlintest.component.NormalRangeCard
import com.example.kotlintest.screens.tonometer.models.TonometerCardData
import com.example.kotlintest.util.horizontalPadding
import com.example.kotlintest.util.verticalPadding

@Composable
fun RowScope.PressureInfoCard(
    pressureValue: TonometerCardData,
    systolicPressure: Double,
    pulseRate: TonometerCardData,
    pressureIcon: Int
) {
    Card(
        modifier = Modifier
            .padding(bottom = 50.dp)
            .weight(1f),
        colors = CardDefaults
            .cardColors(
                containerColor = Color.White
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        )
        {
            IndicatorAndSDValues(
                systolicPressure = systolicPressure,
                pulseRate = pulseRate.value.toDouble(),
                pressureIcon = pressureIcon
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .horizontalPadding(25)
                    .verticalPadding(12),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NormalRangeCard(
                    value = pressureValue.value,
                    cardGeneralColor = pressureValue.cardColor,
                    cardUnitColor = pressureValue.cardUnitColor,
                    title = pressureValue.title,
                    normalRange = pressureValue.range,
                    unit = pressureValue.cardUnit.unit,
                    valueFontSize = 30,
                    normalRangeFontSize = 10,
                    boxModifier = Modifier.height(150.dp),
                    unitFontSize = 12,
                    valueTextAlignment = Alignment.CenterEnd
                )
                NormalRangeCard(
                    value = pulseRate.value,
                    cardGeneralColor = pulseRate.cardColor,
                    cardUnitColor = pulseRate.cardUnitColor,
                    title = pulseRate.title,
                    normalRange = pulseRate.range,
                    unit = pulseRate.cardUnit.unit,
                    valueFontSize = 30,
                    normalRangeFontSize = 10,
                    boxModifier = Modifier.height(150.dp),
                    unitFontSize = 12,
                    valueTextAlignment = Alignment.Center
                )
            }
        }
    }
}
