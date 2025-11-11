package com.example.kotlintest.screens.tonometer.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.screens.tonometer.PatientBodyPart
import com.example.kotlintest.screens.tonometer.TonometerState
import com.example.kotlintest.ui.theme.rhDisplayMedium

@Composable
fun RowScope.LampWithIcon(
    uiState: TonometerState,
    onLambChange: (PatientBodyPart) -> Unit = {},
    ) {
    Column(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 30.dp)
            .weight(0.7f)
    )
    {
        Text(
            text = "Select Place & Type  To Put On Cufft",
            style = MaterialTheme.typography.rhDisplayMedium.copy(
                fontSize = 14.sp
            )
        )

        Box(modifier = Modifier.wrapContentWidth()) {
            LeftLamb(uiState, onLambChange)
            Icon(
                painter = painterResource(uiState.patientIcon),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(120.dp)
                    .width(100.dp)
                    //.padding(end =30.dp)
            )
            RightLamp(uiState, onLambChange)
        }
    }
}