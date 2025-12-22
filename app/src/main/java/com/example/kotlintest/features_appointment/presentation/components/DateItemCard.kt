package com.example.kotlintest.features_appointment.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.util.CustomDateTimeFormatter
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.scalePxToDp


@Composable
fun DateItemCard(
    dateTime: String = "2025-12-11T00:00:00+03:00",
    slotsNum: Int = 4,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(scalePxToDp(230f))
            .height(scalePxToDp(80f))

    ) {
        CustomItemCard(
            modifier = Modifier.matchParentSize(),
            selected = selected,
            onClick = onClick,
            content = {
                DateItemCardContent(
                    dateTime = dateTime,
                    slotsNum = slotsNum
                )
            }
        )
    }
}

@Composable
fun DateItemCardContent(
    dateTime: String = "",
    slotsNum: Int = 4,
)
{
    Box(
        modifier = Modifier
            .padding(start = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = CustomDateTimeFormatter.formatDate(dateTime),
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = YankeesBlue,
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = "$slotsNum available slots",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 7.sp
                )
            )

        }

    }
}


@Preview
@Composable
fun DateItemCardPreview() {
    DateItemCard()
}