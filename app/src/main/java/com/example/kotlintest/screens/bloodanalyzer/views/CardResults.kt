package com.example.kotlintest.screens.bloodanalyzer.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlintest.component.CardWithShadowOnBorder
import com.example.kotlintest.screens.bloodanalyzer.models.WhiteBloodCellAnalyzerResult
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayRegular
import com.example.kotlintest.util.horizontalPadding
import com.example.kotlintest.util.verticalPadding

@Composable
fun CardResults(item: WhiteBloodCellAnalyzerResult) {
    CardWithShadowOnBorder(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalPadding(20)
                .verticalPadding(25)
        ) {
            CardHeader(item.testNo, item.date, item.testNoTitle)
            Spacer(modifier = Modifier.height(15.dp))
            SingleRow(
                item.item,
                item.result,
                item.units,
                item.reference,
                style = MaterialTheme.typography.rhDisplayBold
            )
            Spacer(modifier = Modifier.height(15.dp))
            item.cardValues.forEachIndexed { index, it ->
                SingleRow(
                    item = it.item,
                    result = it.result,
                    units = it.units,
                    reference = it.reference,
                    style = MaterialTheme.typography.rhDisplayRegular
                )
                if (index != item.cardValues.lastIndex) {
                    Spacer(modifier = Modifier.height(5.dp))
                }

            }
        }
    }
}
