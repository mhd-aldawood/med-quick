package com.example.kotlintest.features_home.presentation.screens.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kotlintest.NavDestination
import com.example.kotlintest.R
import com.example.kotlintest.features_home.presentation.components.FilterChipGroup
import com.example.kotlintest.navigation.safeNavigate
import com.example.kotlintest.ui.theme.ChineseYellow
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.White
import com.example.kotlintest.ui.theme.deepDarkBlue
import com.example.kotlintest.ui.theme.rhDisplayBlack
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.util.scalePxToDp

@Composable
fun MainHeaderSection(navController: NavController) {
    Text(
        text = "Calender",
        style = MaterialTheme.typography.rhDisplayBlack.copy(
            color = PrimaryMidLinkColor,
            fontSize = 28.sp
        )
    )
    Spacer(Modifier.width(15.dp))
    Row() {
        var onlyMeBtnEnabled by remember { mutableStateOf(true) }
        var thisKitBtnEnabled by remember { mutableStateOf(true) }
        OutlinedButton(
            onClick = { onlyMeBtnEnabled = !onlyMeBtnEnabled },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (onlyMeBtnEnabled) PrimaryMidLinkColor else White,
                contentColor = Lotion,
                disabledContainerColor = Color.White,
                disabledContentColor = PrimaryMidLinkColor
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(7.dp),
            border = BorderStroke(1.dp, deepDarkBlue),
            modifier = Modifier
                .height(scalePxToDp(67f))
                .width(if (onlyMeBtnEnabled) scalePxToDp(190f) else scalePxToDp(160f))

        ) {
            Text(
                text = "Only Me",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = if (onlyMeBtnEnabled) White else PrimaryMidLinkColor,
                    fontSize = 13.sp
                )
            )
            if (onlyMeBtnEnabled) {
                Spacer(Modifier.width(3.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_med_check_circle),
                    contentDescription = "Menu",
                    tint = ChineseYellow,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(scalePxToDp(32f))
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        OutlinedButton(
            onClick = { thisKitBtnEnabled = !thisKitBtnEnabled },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (thisKitBtnEnabled) PrimaryMidLinkColor else White,
                contentColor = Lotion,
                disabledContainerColor = Color.White,
                disabledContentColor = PrimaryMidLinkColor
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(7.dp),
            border = BorderStroke(1.dp, deepDarkBlue),
            modifier = Modifier
                .height(scalePxToDp(67f))
                .width(if (thisKitBtnEnabled) scalePxToDp(190f) else scalePxToDp(160f))

        ) {
            Text(
                text = "This Kit",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = if (thisKitBtnEnabled) White else PrimaryMidLinkColor,
                    fontSize = 13.sp
                )
            )
            if (thisKitBtnEnabled) {
                Spacer(Modifier.width(3.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_med_check_circle),
                    contentDescription = "Menu",
                    tint = ChineseYellow,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(scalePxToDp(32f))
                )
            }
        }
    }
    Row() {
        Spacer(modifier = Modifier.weight(1f))
        FilterChipGroup()
        Spacer(Modifier.width(10.dp))
        OutlinedButton(
            onClick = {
                navController.safeNavigate(NavDestination.APPOINTMENT_CREATE_SCREEN)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryMidLinkColor,
                contentColor = Lotion,
                disabledContainerColor = Color.White,
                disabledContentColor = PrimaryMidLinkColor
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, White),
            modifier = Modifier
                .height(scalePxToDp(70f))
                .width(scalePxToDp(420f))

        ) {
            Icon(
                painter = painterResource(R.drawable.ic_med_create_icon),
                contentDescription = "Menu",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(scalePxToDp(70f))
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Create Appointment",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = Lotion,
                    fontSize = 14.sp
                )
            )
            Spacer(Modifier.width(10.dp))
        }
    }


}