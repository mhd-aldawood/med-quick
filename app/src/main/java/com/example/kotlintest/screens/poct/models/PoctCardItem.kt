package com.example.kotlintest.screens.poct.models

data class PoctCardItem(
    val poctCardTestHeader: PoctCardTestHeader,
    val cardFirstColumn: List<String>,
    val cardSecondColumn: List<Any>,
    val cardThirdColumn: List<String>,
    val cardFourthColumn: List<String>
)