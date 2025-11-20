package com.example.kotlintest.screens.bloodanalyzer.models

data class WhiteBloodCellAnalyzerValues(
    val item: String = "White Blood Cell (WBC)",
    val result: String = "7.5",
    val units: String = "x10^9/L",
    val reference: String = "4.5 - 11.0"
)

data class WhiteBloodCellAnalyzerResult(
    val testNo: String = "1001",
    val testNoTitle: String = "Test No:",
    val date: String = "2024/03026 14:07:13",
    val item: String = "Item",
    val result: String = "Result",
    val units: String = "Units",
    val reference: String = "Reference",
    val cardValues: List<WhiteBloodCellAnalyzerValues>
)