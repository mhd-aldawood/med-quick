package com.example.kotlintest.util

import com.example.kotlintest.screens.bloodanalyzer.models.WhiteBloodCellAnalyzerResult
import com.example.kotlintest.screens.bloodanalyzer.models.WhiteBloodCellAnalyzerValues

fun mapWhiteBloodCellAnalyzerResult(
    oldList: List<WhiteBloodCellAnalyzerResult>,
    data: CellResult
): List<WhiteBloodCellAnalyzerResult> {

    val valueMap = mapOf(
        "WBC" to data.wbc,
        "LYM" to data.lym,
        "MON" to data.mon,
        "NEU" to data.neu,
        "EQS" to data.eqs,
        "BAS" to data.bas
    )

    return oldList.map { result ->
        result.copy(
            cardValues = result.cardValues.map { card ->
                val newValue = valueMap[card.item] ?: card.result
                card.copy(result = newValue)
            }.toMutableList()
        )
    }
}

fun createResultFromCell(data: CellResult): List<WhiteBloodCellAnalyzerValues> {
    val values = mutableListOf(
        WhiteBloodCellAnalyzerValues(
            item = "WBC",
            result = data.wbc,
            units = "10^9/L",
            reference = "3.6 - 10.0"
        ),
        WhiteBloodCellAnalyzerValues(
            item = "LYM",
            result = data.lym,
            units = "%",
            reference = "1.0 - 3.7"
        ),
        WhiteBloodCellAnalyzerValues(
            item = "MON",
            result = data.mon,
            units = "%",
            reference = "1.0 - 0.8"
        ),
        WhiteBloodCellAnalyzerValues(
            item = "NEU",
            result = data.neu,
            units = "%",
            reference = "1.5 - 7.0"
        ),
        WhiteBloodCellAnalyzerValues(
            item = "EQS",   // EQS = EOS
            result = data.eqs,
            units = "%",
            reference = "0.1 - 0.5"
        ),
        WhiteBloodCellAnalyzerValues(
            item = "BAS",
            result = data.bas,
            units = "%",
            reference = "0.0 - 0.1"
        )
    )

    return values
}
