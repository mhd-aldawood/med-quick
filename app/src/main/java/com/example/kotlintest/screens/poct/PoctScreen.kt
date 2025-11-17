package com.example.kotlintest.screens.poct

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlintest.component.CardWithShadowOnBorder
import com.example.kotlintest.screens.poct.models.Category
import com.example.kotlintest.screens.poct.views.CardListBody
import com.example.kotlintest.screens.poct.views.CardListHeader
import com.example.kotlintest.ui.theme.Lotion
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayMedium
import com.example.kotlintest.util.horizontalPadding
import com.example.kotlintest.util.verticalPadding

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

@Composable
fun AvailableTestsSection(state: PoctState, modifier: Modifier) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp
    Box(
        modifier = modifier
            .height(screenHeightDp.dp)
            .clip(shape = RoundedCornerShape(32.dp))
            .background(color = Lotion, shape = RoundedCornerShape(32.dp))
            .padding(top = 32.dp, start = 16.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Column 1
            Column(
                modifier = Modifier.weight(1.1f),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            )
            {
                state.list.getOrNull(0)?.let { SectionBox(it) }
                state.list.getOrNull(1)?.let { SectionBox(it) }
            }

            // Column 2
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            )
            {
                state.list.getOrNull(2)?.let { SectionBox(it) }
                state.list.getOrNull(3)?.let { SectionBox(it) }
                state.list.getOrNull(4)?.let { SectionBox(it) }
                state.list.getOrNull(5)?.let { SectionBox(it) }
            }

            // Column 3
            Column(
                modifier = Modifier.weight(0.9f),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            )
            {
                state.list.getOrNull(6)?.let { SectionBox(it) }
                state.list.getOrNull(7)?.let { SectionBox(it) }
            }
        }
    }
}

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

@Composable
fun SectionBox(section: Category) {
    Column {
        Text(
            text = section.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.rhDisplayBold.copy(
                fontSize = 14.sp,
                color = PrimaryMidLinkColor
            )
        )
        Spacer(Modifier.height(6.dp))
        section.items.forEachIndexed { index, it ->
            Text(
                text = it,
                style = MaterialTheme.typography.rhDisplayMedium.copy(
                    fontSize = 12.sp,
                    color = PrimaryMidLinkColor
                )
            )
            if (index != section.items.size - 1)
                Spacer(Modifier.height(8.dp))
        }
    }
}