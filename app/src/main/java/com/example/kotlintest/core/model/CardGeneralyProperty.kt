package com.example.kotlintest.core.model

import androidx.compose.ui.graphics.Color

sealed class CalendarCard(
    open val calendarStatus: List<CalendarCardStatus>,
)