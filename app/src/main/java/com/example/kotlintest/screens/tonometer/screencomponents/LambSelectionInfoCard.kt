package com.example.kotlintest.screens.tonometer.screencomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kotlintest.screens.tonometer.AgeGroup
import com.example.kotlintest.screens.tonometer.ChosenLamp
import com.example.kotlintest.screens.tonometer.PatientBodyPart
import com.example.kotlintest.screens.tonometer.PatientPosition
import com.example.kotlintest.screens.tonometer.PositionType
import com.example.kotlintest.screens.tonometer.SelectStatus
import com.example.kotlintest.screens.tonometer.TonometerState
import com.example.kotlintest.util.horizontalPadding
import com.example.kotlintest.util.verticalPadding

@Composable
fun RowScope.LampSelectionInfoCard(
    uiState: TonometerState,
    onLambChange: (PatientBodyPart) -> Unit,
    onSittingPosChange: (PositionType) -> Unit ,
    onAgeGroupChange: (AgeGroup) -> Unit ,
) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 15.dp, bottom = 50.dp)
            .weight(1f),
        colors = CardDefaults
            .cardColors(
                containerColor = Color.White
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .verticalPadding(28)
                .padding(start = 15.dp)//,end=10.dp),
//            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            LampWithIcon(uiState, onLambChange = { chosenLamp -> onLambChange(chosenLamp) })
            PositionAgeGroup(
                uiState = uiState,
                onSittingPosChange = { positionType -> onSittingPosChange(positionType) },
                onAgeGroupChange = { ageGroup -> onAgeGroupChange(ageGroup) })

        }
    }


}
