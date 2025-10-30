package com.example.kotlintest.screens.tonometer.screencomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.PressureInfoCard(
    pressureValue: String,
    systolicPressure: Float,
    pulseRate: Float,
    pressureIcon: Int
) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 15.dp, bottom = 50.dp)
            .weight(1f), colors = CardDefaults
            .cardColors(
                containerColor = Color.White
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier,
        )
        {
            IndicatorAndSDValues(systolicPressure = systolicPressure, pulseRate = pulseRate,pressureIcon=pressureIcon)
            SDValuesAndText(pressureValue = pressureValue)
        }
    }
}
