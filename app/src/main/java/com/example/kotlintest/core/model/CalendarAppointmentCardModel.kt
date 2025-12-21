package com.example.kotlintest.core.model


data class CalendarAppointmentCardModel(
    val id: String,
    val patientName: String,
    val locationText: String,
    val nurseName: String,
    val kitName: String,
    val date: String,
    val age: String,
    val gender: Gender,
    val selected: Boolean,
    override val calendarStatus: List<CalendarCardStatus>,
) : CalendarCard(calendarStatus, )
