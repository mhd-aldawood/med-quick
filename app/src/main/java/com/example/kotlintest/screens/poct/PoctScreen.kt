package com.example.kotlintest.screens.poct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintest.screens.poct.views.AvailableTestsSection
import com.example.kotlintest.screens.poct.views.CardsListTestsSection

@Composable
//FIA Testing System
fun PoctScreen(viewModel: PoctViewModel, uiState: PoctState, onCheckClicked: () -> Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        CardsListTestsSection(
            viewModel = viewModel, uiState = uiState, modifier = Modifier.weight(0.5f)
        )
        AvailableTestsSection(
            modifier = Modifier.weight(0.5f),
            state = uiState
        )
    }
}

