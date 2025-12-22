package com.example.kotlintest.features_appointment.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.util.CustomDateTimeFormatter
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.White
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.scalePxToDp


@Composable
fun SlotItemCard(
    slotDateTime: String = "2025-12-16T00:00:00",
    slotTime: String = "19:32:48.1368880",
    slotCharge: Int = 140,
    slotChargeCurrency:String ="",
    slotAppointmentDuration:Int= 70,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(scalePxToDp(380f))
            .height(scalePxToDp(85f))

    ) {
        CustomItemCard(
            modifier = Modifier.matchParentSize(),
            selected = selected,
            onClick = onClick,
            content = {
                SlotItemCardContent(
                    slotDateTime = slotDateTime,
                    slotTime = slotTime,
                    slotCharge = slotCharge,
                    slotChargeCurrency = slotChargeCurrency,
                    slotAppointmentDuration = slotAppointmentDuration
                )
            }
        )
    }
}

@Composable
fun SlotItemCardContent(
    slotDateTime: String = "2025-12-11T00:00:00+03:00",
    slotTime: String = "19:32:48.1368880",
    slotCharge: Int = 4,
    slotChargeCurrency:String ="",
    slotAppointmentDuration:Int= 70,
)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 7.dp),
                contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // your content

            Box(
                modifier = Modifier.height(scalePxToDp(60f))
                    .width(scalePxToDp(60f))
                    .background(Periwinkle,
                        shape = RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center,){
                Text(
                text = CustomDateTimeFormatter.formatDateDayOnly(slotDateTime),
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = White,
                    fontSize = 10.sp
                ),
                textAlign = TextAlign.Center,
            )}
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = CustomDateTimeFormatter.convertUtcTimeToLocal(slotDateTime, slotTime),
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = YankeesBlue,
                        fontSize = 13.sp
                    )
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "$slotCharge $slotChargeCurrency | ${slotAppointmentDuration}Min",
                    style = MaterialTheme.typography.rhDisplayMedium.copy(
                        color = PrimaryMidLinkColor,
                        fontSize = 7.sp
                    )
                )

            }
            Spacer(modifier = Modifier.width(3.dp))

        }
    }
}


@Preview
@Composable
fun SlotItemCardPreview() {
    SlotItemCard()
}