package com.example.kotlintest.screens.home.screencomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.component.VerticalSpacer
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold

@Composable
fun ColumnScope.AddDeviceWithPatientInfo(
    onAddDeviceClicked: () -> Unit,
    gender: String,
    age: String,
    insuranceCompany: String,
    addDevices: String,
    addDevicesIcon: Int,
    icon: Int,
    name: String,
    companyName: String,
    insuranceType: String,
    number: String
) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding( top = 10.dp)
            .align(Alignment.End)
            .clickable { onAddDeviceClicked },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Text(
            text = addDevices,
            style = MaterialTheme
                .typography
                .rhDisplayBold
                .copy(fontSize = 15.sp, color = PrimaryMidLinkColor, fontWeight = FontWeight.Bold),
            modifier = Modifier
        )
        Icon(
            painter = painterResource(addDevicesIcon),
            tint = Color.Unspecified, contentDescription = "",
            modifier = Modifier.size(40.dp)
        )
    }
    VerticalSpacer(55)

    PatentInfoSection(
        gender = gender,
        age = age,
        name = name,
        insuranceCompany = insuranceCompany,
        icon = icon,
        companyName = companyName,
        insuranceType = insuranceType,
        number = number
    )
}


