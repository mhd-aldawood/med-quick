package com.example.kotlintest.screens.tonometer.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintest.component.SimpleThermometer
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.util.verticalPadding

@Composable
fun RowScope.IndicatorAndSDValues(systolicPressure: Double, pulseRate: Double, pressureIcon: Int) {
    Box(
        modifier = Modifier
            .weight(0.5f)
            .fillMaxSize()
            .verticalPadding(12),
    )
    {
        SimpleThermometer(
            value = systolicPressure.toFloat(),
            secondValue = pulseRate.toFloat(),
            colorIndicator = PrimaryMidLinkColor,
            incrementNumber = 30,
            minValue = 0f,
            maxValue = 240f,
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .padding(bottom = 7.dp)
                .align(alignment = Alignment.TopStart),
        )
        NormalAbnormal(
            modifier = Modifier
                .width(
                    150.dp
                )
                .padding(start = 20.dp)
                .align(alignment = Alignment.BottomEnd)
        )
        SDevalues(
            pressureIcon = pressureIcon,
            systolicPressure = systolicPressure,
            pulseRate = pulseRate
        )
    }
}