package com.example.kotlintest.screens.tonometer.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.component.Ring
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.SpanishGray
import com.example.kotlintest.ui.theme.rhDisplayMedium

@Composable
fun NormalAbnormal(modifier: Modifier) {

    Row(
        modifier=modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RingAndText(ringColor = FrenchWine, text = "Normal")
        RingAndText(ringColor = PrimaryMidLinkColor, text = "AbNormal")
    }
}
@Composable
fun RingAndText(ringColor: Color, text:String){
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp),) {
        Ring(
            modifier = Modifier.size(10.dp),
            ringColor = ringColor,
            ringWidth = 2.dp
        )
        Text(
            text = text, style = MaterialTheme
                .typography
                .rhDisplayMedium
                .copy(fontSize = 10.sp, color = SpanishGray)
        )
    }

}