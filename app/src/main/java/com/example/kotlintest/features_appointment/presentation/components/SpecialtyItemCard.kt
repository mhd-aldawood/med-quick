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
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.scalePxToDp


@Composable
fun SpecialtyItemCard(
    specialtyName: String = "",
    doctorsAvailableNum: Int = 4,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.width(scalePxToDp(340f))
            .height(scalePxToDp(114f)),
    ) {
        CustomItemCard(
            modifier = Modifier.matchParentSize(),
            selected = selected,
            onClick = onClick,
            content = {
                SpecialtyItemCardContent(
                    specialtyName = specialtyName,
                    doctorsAvailableNum = doctorsAvailableNum
                )
            }
        )
    }
}

@Composable
fun SpecialtyItemCardContent(
    specialtyName: String = "",
    doctorsAvailableNum: Int = 4,
)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // your content
            Image(
                painter = painterResource(R.drawable.ic_med_specialty_icon),
                contentDescription = null,
                modifier = Modifier.height(scalePxToDp(112f))
                    .width(scalePxToDp(82f)),
                contentScale = ContentScale.FillWidth

            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = specialtyName,
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = YankeesBlue,
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "$doctorsAvailableNum Doctors available",
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
fun SpecialtyItemCardPreview() {
    SpecialtyItemCard()
}