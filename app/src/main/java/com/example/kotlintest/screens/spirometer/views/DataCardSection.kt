package com.example.kotlintest.screens.spirometer.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.component.CardWithShadowOnBorder
import com.example.kotlintest.screens.spirometer.models.SpirometerResult
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.Typography
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.horizontalPadding

@Composable
fun RowScope.DataCardSection(cardHeader: List<String>, cardResult: List<SpirometerResult>) {
    CardWithShadowOnBorder(
        modifier = Modifier
            .weight(0.4f)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top=20.dp, bottom = 20.dp)
        ) {

            SpirometerCardRow(
                cardHeader,
                paddingBottomValue = 10,
                textStyle = Typography.rhDisplayBlack.copy(fontSize = 25.sp, color = CeruleanBlue,
                    textAlign = TextAlign.Center
                )
            )

            cardResult.forEach {
                SpirometerCardRow(
                    listOf(it.par, it.act, it.pre),
                    paddingBottomValue = 5,
                    textStyle = Typography.rhDisplayMedium.copy(
                        fontSize = 15.sp,
                        color = PrimaryMidLinkColor,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }

}


@Composable
fun SpirometerCardRow(list: List<String>, paddingBottomValue: Int, textStyle: TextStyle) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = paddingBottomValue.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        list.forEach {
            Text(text = it, style = textStyle, modifier = Modifier.weight(1f))
        }

    }
}