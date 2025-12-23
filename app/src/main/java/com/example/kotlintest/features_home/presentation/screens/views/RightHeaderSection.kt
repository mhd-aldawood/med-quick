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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.White
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.yellow
import com.example.kotlintest.util.scalePxToDp

@Composable
fun RightHeaderSection() {
    Row() {
        OutlinedButton(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = yellow,
                contentColor = Lotion,
                disabledContainerColor = Color.White,
                disabledContentColor = PrimaryMidLinkColor
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, White),
            modifier = Modifier
                .height(scalePxToDp(70f))
                .width(scalePxToDp(310f))

        ) {
            Icon(
                painter = painterResource(R.drawable.ic_med_ongoing_visit_icon),
                contentDescription = "Menu",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(scalePxToDp(70f))
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Ongoing Visit",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    color = Lotion,
                    fontSize = 14.sp
                )
            )
            Spacer(Modifier.width(10.dp))
        }
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(R.drawable.ic_med_notification_icon),
            contentDescription = "Menu",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(scalePxToDp(70f))
        )

    }

}