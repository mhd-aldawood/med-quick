package com.example.kotlintest.screens.home.screencomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayMedium

@Composable
fun ColumnScope.PatentInfoSection(
    gender: String,
    age: String,
    insuranceCompany: String,
    icon: Int,
    name: String,
    companyName: String,
    insuranceType: String,
    number: String
) {
    val cni = listOf<String>(companyName, insuranceType, number)
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ), colors = CardDefaults
            .cardColors(
                containerColor = Color.White
            ),
        modifier = Modifier
            .wrapContentWidth()
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Patient Info",
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = PrimaryMidLinkColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                HorizontalSpacer(30)
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "",
                    modifier = Modifier.size(15.dp),
                    tint = Color.Unspecified
                )
            }
            Text(
                text = name,
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = CeruleanBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = "$age, $gender",
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    color = CeruleanBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(top = 6.dp)
            )
            Spacer(modifier = Modifier.height(17.dp))
            Text(
                text = "Patient Info",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = PrimaryMidLinkColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(10.dp))

            cni.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.rhDisplayMedium.copy(
                        color = CeruleanBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

        }


    }
}
