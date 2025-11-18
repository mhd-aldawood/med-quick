package com.example.kotlintest.screens.poct.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.kotlintest.screens.poct.PoctState
import com.example.kotlintest.ui.theme.Lotion

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

