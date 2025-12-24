package com.example.kotlintest.core.model

import androidx.compose.ui.unit.Dp
data class PositionedCard(
    val model: CalendarAppointmentCardModel,
    val dayIndex: Int,
    val lane: Int,
    val top: Dp,
    val height: Dp
)