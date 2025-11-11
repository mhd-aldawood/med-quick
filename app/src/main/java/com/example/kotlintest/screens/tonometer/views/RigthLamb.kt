package com.example.kotlintest.screens.tonometer.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.kotlintest.screens.tonometer.PatientBodyPart
import com.example.kotlintest.screens.tonometer.TonometerState
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold

@Composable
fun BoxScope.RightLamp(uiState: TonometerState, onLambChange: (PatientBodyPart) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.TopEnd),
    )
    {
        uiState.rightLamp.forEach {
                it->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = it.text,
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        fontSize = it.fontSize.sp,
                        color = PrimaryMidLinkColor
                    ),
                    modifier = Modifier.clickable { onLambChange(it) }
                )
            }
        }
    }
}