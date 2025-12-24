package com.example.kotlintest.features_home.presentation.screens.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import com.example.kotlintest.component.CardWithShadowOnBorder
import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import com.example.kotlintest.ui.theme.PaleCerulean
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.White
import com.example.kotlintest.ui.theme.YankeesBlue
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.ui.theme.rhDisplayRegular
import com.example.kotlintest.ui.theme.rhDisplaySemiBold
import com.example.kotlintest.ui.theme.rhDisplaySemiBoldItalic
import com.example.kotlintest.util.horizontalPadding
import com.example.kotlintest.util.verticalPadding

@Composable
fun RightContentSection(
    model: CalendarAppointmentCardModel,
    onBtnClick: (String) -> Unit = {}
) {//TODO refactor this component when the api done
    val textWithIconList = listOf(
        Pair<Int, String>(R.drawable.ic_mic, "3:57 record"),
        Pair<Int, String>(R.drawable.ic_pdf, "My last Prescription.pdf"),
        Pair<Int, String>(R.drawable.ic_photo, "My Xray image.jpeg")
    )
    CardWithShadowOnBorder(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 18.dp)
                .horizontalPadding(12)
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            )
            {
                Image(
                    // Replace with your actual drawable resource ID
                    painter = painterResource(id = R.mipmap.ic_person),
                    contentDescription = "Circle Image",
                    contentScale = ContentScale.Crop, // Crops the image to fill the bounds
                    modifier = Modifier
                        .size(55.dp) // Set a specific size (width and height must be equal for a perfect circle)
                        .clip(CircleShape) // Clips the image to a circle shape
                        .border(
                            1.dp,
                            PrimaryMidLinkColor,
                            CircleShape
                        )
                        .padding(3.dp) // Optional: Adds a border around the circle
                )
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Pending",
                        style = MaterialTheme.typography.rhDisplayRegular.copy(
                            fontSize = 9.sp,
                            color = PrimaryMidLinkColor
                        )
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_warning),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                text = model.patientName,
                style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                    fontSize = 18.sp,
                    color = PrimaryMidLinkColor
                )
            )
            Text(
                text = "${model.gender.name} | ${model.calculateAge()}Y",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    fontSize = 10.sp,
                    color = PrimaryMidLinkColor
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = model.locationText,
                style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                    fontSize = 9.sp,
                    color = PrimaryMidLinkColor
                )
            )
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Icon(
                    painter = painterResource(R.drawable.ic_location),
                    modifier = Modifier.size(width = 5.dp, height = 7.dp),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
                Text(
                    text = model.locationText,
                    style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                        fontSize = 9.sp,
                        color = PrimaryMidLinkColor
                    )
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Dr. Omar Senan  |  General Practitioner ",
                style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                    fontSize = 10.sp,
                    color = Periwinkle
                )
            )
            Spacer(modifier = Modifier.height(3.dp))
            Row() {
                Text(
                    text = "Checking: ",
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        fontSize = 10.sp,
                        color = PrimaryMidLinkColor
                    )
                )
                Text(
                    text = "Checking coverage",
                    style = MaterialTheme.typography.rhDisplayBlack.copy(
                        fontSize = 10.sp,
                        color = PrimaryMidLinkColor
                    )
                )
            }
            Text(
                text = "Insurance company: Insurance Type",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    fontSize = 10.sp,
                    color = PrimaryMidLinkColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Compliance from the patient that he has filled when booking the appointment here so the nurse can check Compliance from the patient that he has filled when booking the appointment here so the nurse can check Compliance from the patient that he has filled when booking the appointment …",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    fontSize = 10.sp,
                    color = PrimaryMidLinkColor
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            textWithIconList.forEach {
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(
                        painter = painterResource(id = it.first),
                        tint = Color.Unspecified,
                        contentDescription = null,
                        modifier = Modifier.size(width = 8.dp, height = 8.dp)
                    )
                    Text(
                        text = it.second,
                        style = MaterialTheme.typography.rhDisplaySemiBoldItalic.copy(
                            color = YankeesBlue,
                            fontSize = 8.sp
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = model.nurseName,
                        style = MaterialTheme.typography.rhDisplayBold.copy(
                            color = PrimaryMidLinkColor,
                            fontSize = 10.sp
                        )
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_kit),
                            tint = Color.Unspecified,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = model.kitName,
                            style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                                fontSize = 10.sp,
                                color = PaleCerulean
                            )
                        )
                    }
                }
                Column() {
                    Text(
                        text = model.date,
                        style = MaterialTheme.typography.rhDisplayBold.copy(
                            fontSize = 12.sp,
                            color = PrimaryMidLinkColor
                        )
                    )
                    Text(
                        text = model.startAt,
                        style = MaterialTheme.typography.rhDisplayBold.copy(
                            fontSize = 20.sp,
                            color = PrimaryMidLinkColor
                        )
                    )

                }
            }
            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = "Appointment Details",
                modifier = Modifier
                    .background(color = PrimaryMidLinkColor, shape = RoundedCornerShape(15.dp))
                    .clip(shape = RoundedCornerShape(15.dp))
                    .verticalPadding(8)
                    .horizontalPadding(20)
                    .clickable { onBtnClick.invoke(model.id) },
                style = MaterialTheme.typography.rhDisplayBold.copy(fontSize = 10.sp, color = White)
            )

        }
    }
}