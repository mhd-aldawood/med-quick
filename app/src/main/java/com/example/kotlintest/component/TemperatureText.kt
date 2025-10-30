package com.example.kotlintest.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBlack

enum class TemperatureUnit(val value: String) {
    Celsius("C"),
    Fahrenheit("F")
}
@Composable
fun TemperatureText(temp: Float,fontSize:Int,verticalSpacer:Int=50,unit:TemperatureUnit){
    Row() {
        Text(
            text = temp.toString(), style = MaterialTheme
                .typography
                .rhDisplayBlack.copy(color = PrimaryMidLinkColor, fontSize =fontSize.sp)        )
        HorizontalSpacer(verticalSpacer)
        Text(
            text = unit.value, style = MaterialTheme
                .typography
                .rhDisplayBlack.copy(color = PrimaryMidLinkColor, fontSize = fontSize.sp)
        )
    }
}