package com.example.kotlintest.core.model


data class CalendarMoreCardModel(
    val count: String,
    override val calendarStatus: List<CalendarCardStatus>,
) : CalendarCard(calendarStatus)
