package com.example.kotlintest.screens.theremometer.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.component.Ring
import com.example.kotlintest.component.SimpleThermometer
import com.example.kotlintest.component.TemperatureText
import com.example.kotlintest.component.TemperatureUnit
import com.example.kotlintest.component.VerticalSpacer
import com.example.kotlintest.ui.theme.LavenderGray
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.poppinsMedium
import com.example.kotlintest.util.celsiusToFahrenheit
import com.example.kotlintest.util.horizontalPadding

@Composable
fun TemperatureCard(temperature: Float,normalRange:String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 21.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 21.dp)
        ) {
            SimpleThermometer(
                value = temperature,
                modifier = Modifier
                    .padding(bottom = 55.dp)
                    .height(400.dp)
                    .width(100.dp)
            )
            HorizontalSpacer(20)
            ElevatedCard(
                modifier = Modifier
                    .wrapContentWidth(), colors = CardDefaults
                    .cardColors(
                        containerColor = Color.White
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 65.dp, bottom = 20.dp)
                        .horizontalPadding(115),
                    verticalArrangement = Arrangement.spacedBy((-20).dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Ring(
                        modifier = Modifier
                            .size(30.dp)
                            .offset(x = 40.dp),
                        ringColor = PrimaryMidLinkColor,
                        ringWidth = 5.dp
                    )
                    TemperatureText(temperature, fontSize = 100, unit = TemperatureUnit.Celsius)
                    VerticalSpacer(100)
                    Ring(
                        modifier = Modifier
                            .size(10.dp)
                            .offset(x = 9.dp, y = -16.dp),
                        ringColor = PrimaryMidLinkColor,
                        ringWidth = 2.dp
                    )
                    TemperatureText(temp = celsiusToFahrenheit(temperature),
                        fontSize = 25 , verticalSpacer = 20, unit = TemperatureUnit.Fahrenheit)
                    VerticalSpacer(180)
                    Text(text = normalRange, style = MaterialTheme.typography.poppinsMedium.copy(color = LavenderGray))
                }

            }

        }

    }
}