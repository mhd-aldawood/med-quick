package com.example.kotlintest.screens.pulseoximeter.models

import androidx.compose.ui.graphics.Color

data class PulseOximeterCard(
    val value: String,
    val valueColor: Color? = null,
    val title: String,
    val cardColor: Color,
    val cardUnit: String,
    val cardUnitColor: Color? = null,
    val normalRange: String
)