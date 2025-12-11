package com.example.kotlintest.screens.tonometer.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.ui.theme.DarkSilver
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplaySemiBold

@Composable
fun ColumnScope.SDValuesAndText(pressureValue: String)
{
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize()
            .weight(0.5f)
            .padding(top = 33.dp, bottom = 33.dp, end = 10.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = Lotion
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, bottom = 15.dp)) {
            Text(
                text = "Pressure",
                style = MaterialTheme.typography.rhDisplaySemiBold.copy(
                    fontSize = 20.sp,
                    color = PrimaryMidLinkColor
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                textAlign = TextAlign.Center

            )
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = pressureValue,
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        fontSize = 60.sp,
                        color = PrimaryMidLinkColor
                    ),
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                )
                Text(
                    text = "mm Hg",
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        fontSize = 13.sp,
                        color = PrimaryMidLinkColor
                    ),
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                )
            }

            Text(
                text = "Mean      100      mm Hg",
                style = MaterialTheme.typography.rhDisplayBold.copy(
                    fontSize = 13.sp,
                    color = DarkSilver
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                textAlign = TextAlign.Center,
            )
        }
    }
}