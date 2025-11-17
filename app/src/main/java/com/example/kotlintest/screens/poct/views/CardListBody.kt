package com.example.kotlintest.screens.poct.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.kotlintest.screens.poct.models.PoctCardItem
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.LaSalleGreen
import com.example.kotlintest.ui.theme.SpaceCadet
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayRegular


@Composable
fun CardListBody(poctCardItem: PoctCardItem) {

    val maxRows = listOf(
        poctCardItem.cardFirstColumn.size,
        poctCardItem.cardSecondColumn.size,
        poctCardItem.cardThirdColumn.size,
        poctCardItem.cardFourthColumn.size
    ).max()

    Column(modifier = Modifier.fillMaxWidth()) {

        for (row in 0 until maxRows) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ---------- FIRST COLUMN ----------
                Text(
                    text = poctCardItem.cardFirstColumn.getOrNull(row) ?: "",
                    style = MaterialTheme.typography.rhDisplayBold.copy(
                        color = SpaceCadet,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier
                        .weight(1f)
                )

                // ---------- SECOND COLUMN ----------
                val second = poctCardItem.cardSecondColumn.getOrNull(row)
                val (value, color) = when (second) {
                    is Pair<*, *> -> {
                        val txt = second.first as? String ?: ""
                        val isHigh = second.second as? Boolean ?: false
                        txt to if (isHigh) FrenchWine else LaSalleGreen
                    }

                    is String -> second to SpaceCadet
                    else -> "" to SpaceCadet
                }

                Text(
                    text = value,
                    style = MaterialTheme.typography.rhDisplayRegular.copy(
                        color = color,
                        fontSize = 12.sp
                    ),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(0.7f)
                )

                // ---------- THIRD COLUMN ----------
                Text(
                    text = poctCardItem.cardThirdColumn.getOrNull(row) ?: "",
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.rhDisplayRegular.copy(
                        color = SpaceCadet,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .weight(1.3f)
                )

                // ---------- FOURTH COLUMN ----------
                Text(
                    text = poctCardItem.cardFourthColumn.getOrNull(row) ?: "",
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.rhDisplayRegular.copy(
                        color = SpaceCadet,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

