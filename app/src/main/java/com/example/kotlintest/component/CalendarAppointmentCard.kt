package com.example.kotlintest.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplaySemiBold
import com.example.kotlintest.util.horizontalPadding
import com.example.kotlintest.util.verticalPadding
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.PaleCerulean
import com.example.kotlintest.ui.theme.SpaceCadet

@Composable
fun CalendarAppointmentCard(modifier:Modifier=Modifier,card: CalendarAppointmentCardModel) {
    val selectedColor = if (card.selected) PrimaryMidLinkColor else Lotion//border-patient-startTime
    val notSelectedColor = if (card.selected) Lotion else PrimaryMidLinkColor
    val backgroundColor = if (!card.selected) Periwinkle else Lotion//gender-age-divider
    val locationColor = if (card.selected) Lotion else SpaceCadet
    BoxWithConstraints {
        val maxWidth = minOf(maxWidth, 235.dp)
        Box(
            modifier = modifier
                .width(maxWidth)
                .heightIn(min = 100.dp, max = 150.dp)
                .border(shape = RoundedCornerShape(12.dp), color = PrimaryMidLinkColor, width = 1.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(selectedColor)
                .verticalPadding(12)
                .horizontalPadding(12)
        )
        {

            Column(modifier = Modifier.align(alignment = Alignment.TopStart), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                )
                {
                    Text(
                        text = card.patientName,
                        modifier= Modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                            fontSize = 10.sp,
                            color = notSelectedColor
                        )
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(8.dp)
                            .width(1.dp),
                        color = backgroundColor
                    )
                    Text(
                        text = card.gender.gender,
                        style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                            fontSize = 6.sp,
                            color = backgroundColor
                        )
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(8.dp)
                            .width(1.dp),
                        color = backgroundColor
                    )
                    Text(
                        text = card.dateOfBirth,
                        style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                            fontSize = 6.sp,
                            color = backgroundColor
                        )
                    )

                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_location),
                        contentDescription = null,
                        tint = locationColor,
                        modifier= Modifier.size(width = 10.dp, height = 10.dp)
                    )
                    Text(
                        text = card.locationText,
                        style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                            fontSize = 8.sp,
                            color = locationColor
                        )
                    )
                }
            }

            Column(modifier = Modifier.align(alignment = Alignment.BottomStart)) {
                Text(text = card.nurseName, style = MaterialTheme.typography.rhDisplaySemiBold.copy(fontSize = 8.sp,color=PaleCerulean))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(painter = painterResource(R.drawable.ic_kit),modifier= Modifier.size(width = 10.dp, height = 8.dp), contentDescription = null, tint = PaleCerulean)
                    Text(text = card.kitName, style = MaterialTheme.typography.rhDisplaySemiBold.copy(fontSize = 8.sp,color=PaleCerulean))
                }
            }

            Row(modifier = Modifier.align(alignment = Alignment.TopEnd), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                card.calendarStatus.forEach { status ->
                    Image(painter = painterResource(status.icon),contentDescription = null, modifier= Modifier.size(8.dp))
                }
            }

            Text(
                text = card.date,
                modifier = Modifier.align(Alignment.BottomEnd),
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    fontSize = 12.sp,
                    color = notSelectedColor
                )
            )

        }
    }


}
@Preview
@Composable
fun CalendarAppointmentCardPreview(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()){
        CalendarAppointmentCard(card = AppointmentList[0] as CalendarAppointmentCardModel)
    }
}
