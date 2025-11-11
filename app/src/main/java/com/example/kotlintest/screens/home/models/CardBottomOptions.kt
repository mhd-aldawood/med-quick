package com.example.kotlintest.screens.home.models

import androidx.compose.ui.graphics.Color
import com.example.kotlintest.ui.theme.ChineseYellow
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor


sealed class CardBottomOptions() {
    data class SeeResult(val textColor: Color = ChineseYellow, val text: String = "See Result") :
        CardBottomOptions()

    data object Empty : CardBottomOptions()
    data class Temperature(
        val text: String = "36.5°C",
        val textColor: Color = PrimaryMidLinkColor
    ) : CardBottomOptions()

    data class NewPulse(
        val list: List<PulseType> = listOf(
            PulseType(
                text = "SpO2",
                textColor = FrenchWine,
                value = "95"
            ), PulseType(text = "Pulse", textColor = PrimaryMidLinkColor, value = "75")
        )
    ) : CardBottomOptions()
}
