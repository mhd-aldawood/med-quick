package com.example.kotlintest.screens.bloodanalyzer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.kotlintest.component.NormalRangeCard
import com.example.kotlintest.screens.bloodanalyzer.views.CardResultsList

@Composable
fun BloodAnalyzerScreen(uiState: BloodAnalyzerState, analyzerViewModel: BloodAnalyzerViewModel) {

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(60.dp)) {
        CardResultsList(modifier = Modifier.weight(0.5f), uiState = uiState)
        CardWithPhoto(modifier = Modifier.weight(0.5f), uiState = uiState)
    }

}

@Composable
fun CardWithPhoto(modifier: Modifier, uiState: BloodAnalyzerState) {
    val screenHeigth = LocalConfiguration.current.screenHeightDp
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        NormalRangeCard(
            cardGeneralColor = uiState.pulseCard.cardColor,
            unit = uiState.pulseCard.cardUnit,
            title = uiState.pulseCard.title,
            normalRange = uiState.pulseCard.normalRange,
            value = uiState.pulseCard.value,
            boxModifier = Modifier
                .fillMaxWidth()
                .height((screenHeigth / 1.6).dp),
            valueFontSize = 100
        )
        NormalRangeCard(
            cardGeneralColor = uiState.pulseCard.cardColor,
            unit = uiState.pulseCard.cardUnit,
            title = uiState.pulseCard.title,
            normalRange = uiState.pulseCard.normalRange,
            value = uiState.pulseCard.value,
            boxModifier = Modifier
                .fillMaxWidth()
                .height((screenHeigth / 1.6).dp),
        )

    }
}




