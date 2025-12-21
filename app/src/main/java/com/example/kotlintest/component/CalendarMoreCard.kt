package com.example.kotlintest.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.core.model.CalendarMoreCardModel
import com.example.kotlintest.ui.theme.PaleCerulean
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplaySemiBold

@Composable
fun CalendarMoreCard(calendarMoreCard: CalendarMoreCardModel) {
    Box(
        modifier = Modifier
            .size(width = 75.dp, height = 85.dp)
            .background(color = PaleCerulean, shape = RoundedCornerShape(12.dp))
            .clip(shape = RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Text(
            text = calendarMoreCard.count,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                fontSize = 12.sp,
                color = PrimaryMidLinkColor
            )
        )
        calendarMoreCard.calendarStatus.forEachIndexed { index, status ->
            Image(
                painter = painterResource(status.icon), modifier = Modifier.size(15.dp)
                    .align(
                        Alignment.TopEnd
                    )
                    .offset(x = ((index * -6)).dp), contentDescription = null
            )
        }

    }
}

@Preview
@Composable
fun CalendarMoreCardPreview(modifier: Modifier = Modifier) {
    CalendarMoreCard(AppointmentList[4] as CalendarMoreCardModel)
}
