package com.example.kotlintest.screens.tonometer.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayRegular

@Composable

fun BoxScope.SDevalues(pressureIcon: Int, systolicPressure: Double, pulseRate: Double) {
    Row(modifier = Modifier
        .align(Alignment.Center)
        .padding(start = 75.dp)
        .wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(pressureIcon),
            tint = Color.Unspecified,
            contentDescription = "",
            modifier = Modifier.height(70.dp)

        )
        Column(modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(26.dp))
        {
            Text(text = systolicPressure.toInt().toString(),
                style = MaterialTheme
                    .typography
                    .rhDisplayRegular
                    .copy(fontSize = 30.sp,color=PrimaryMidLinkColor))
            Text(text = pulseRate.toInt().toString(),
                style = MaterialTheme
                    .typography
                    .rhDisplayRegular
                    .copy(fontSize =30.sp ,color=PrimaryMidLinkColor))
        }
    }
}