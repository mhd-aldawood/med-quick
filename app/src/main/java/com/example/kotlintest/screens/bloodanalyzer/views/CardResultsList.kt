package com.example.kotlintest.screens.bloodanalyzer.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintest.screens.bloodanalyzer.BloodAnalyzerState

@Composable
fun CardResultsList(modifier: Modifier, uiState: BloodAnalyzerState) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(uiState.bloodCellAnalyzerResults) { item ->
            CardResults(item = item)
        }
    }
}