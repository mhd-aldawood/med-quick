package com.example.kotlintest.features_appointment.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.ui.theme.rhDisplaySemiBold
import com.example.kotlintest.util.scalePxToDp


@Composable
fun DoctorItemCard(
    doctorName: String = "Dr. Aghyad Hamadah",
    doctorCharge: Int = 4,
    doctorChargeCurrency:String ="",
    appointmentDuration:Int= 70,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.width(scalePxToDp(380f))
            .height(scalePxToDp(85f)),
    ) {
        CustomItemCard(
            modifier = Modifier.matchParentSize(),
            selected = selected,
            onClick = onClick,
            content = {
                DoctorItemCardContent(
                    doctorName = doctorName,
                    doctorCharge = doctorCharge,
                    doctorChargeCurrency = doctorChargeCurrency,
                    appointmentDuration = appointmentDuration
                )
            }
        )
    }
}

@Composable
fun DoctorItemCardContent(
    doctorName: String = "",
    doctorCharge: Int = 4,
    doctorChargeCurrency:String ="",
    appointmentDuration:Int= 70,
)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // your content
            Image(
                painter = painterResource(R.drawable.ic_med_doctor_img),
                contentDescription = null,
                modifier = Modifier.height(scalePxToDp(85f))
                    .width(scalePxToDp(56f)),
                contentScale = ContentScale.FillWidth

            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = doctorName,
                    style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                        color = Periwinkle,
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "$doctorCharge $doctorChargeCurrency | ${appointmentDuration}Min",
                    style = MaterialTheme.typography.rhDisplayMedium.copy(
                        color = PrimaryMidLinkColor,
                        fontSize = 8.sp
                    )
                )

            }
            Spacer(modifier = Modifier.width(3.dp))

        }
    }
}


@Preview
@Composable
fun DoctorItemCardPreview() {
    DoctorItemCard()
}