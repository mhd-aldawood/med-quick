package com.example.kotlintest.screens.poct.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintest.component.CardWithShadowOnBorder
import com.example.kotlintest.screens.poct.PoctState
import com.example.kotlintest.screens.poct.PoctViewModel
import com.example.kotlintest.util.horizontalPadding
import com.example.kotlintest.util.verticalPadding

@Composable
fun CardsListTestsSection(viewModel: PoctViewModel, uiState: PoctState, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        uiState.poctCardItems.forEachIndexed { index, poctCardItem ->
            CardWithShadowOnBorder(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalPadding(20)
                        .horizontalPadding(15),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CardListHeader(
                        poctCardItem.poctCardTestHeader.headerTitle,
                        poctCardItem.poctCardTestHeader.headerDate
                    )
                    CardListBody(poctCardItem)
                }

            }
            if (index != uiState.poctCardItems.size - 1)
                VerticalDivider(modifier = Modifier.padding(vertical = 10.dp))
        }
    }


}

