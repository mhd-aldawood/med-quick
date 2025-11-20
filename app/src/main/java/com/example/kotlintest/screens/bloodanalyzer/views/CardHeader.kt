package com.example.kotlintest.screens.bloodanalyzer.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayExtraBold
import com.example.kotlintest.ui.theme.rhDisplayRegular

@Composable
fun CardHeader(testNo: String, date: String, testNoTitle: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "$testNoTitle $testNo",
            style = MaterialTheme.typography.rhDisplayExtraBold.copy(
                fontSize = 18.sp,
                color = PrimaryMidLinkColor
            )
        )
        Text(
            text = date,
            style = MaterialTheme.typography.rhDisplayRegular.copy(
                fontSize = 18.sp,
                color = PrimaryMidLinkColor
            )
        )

    }
}