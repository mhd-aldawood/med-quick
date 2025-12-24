package com.example.kotlintest.core.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField


data class CalendarAppointmentCardModel(
    val id: String,
    val patientName: String,
    val locationText: String,
    val nurseName: String,
    val kitName: String,
    val date: String,//12-23-2025
    val startAt: String,//H:mm:ss
    val endAt: String,//H:mm:ss
    val dateOfBirth: String,
    val gender: Gender,
    val selected: Boolean,
    val image: String? = null,
    override val calendarStatus: List<CalendarCardStatus>,
) : CalendarCard(calendarStatus) {
    companion object {
        val DATE_FORMATTER: DateTimeFormatter =
            DateTimeFormatter.ofPattern("M-d-yyyy")

        val FLEXIBLE_TIME_FORMATTER: DateTimeFormatter =
            DateTimeFormatterBuilder()
                .appendPattern("H:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                .optionalEnd()
                .toFormatter()
    }

    val startDateTime: LocalDateTime
        get() = LocalDateTime.of(
            LocalDate.parse(date, DATE_FORMATTER),
            LocalTime.parse(startAt, FLEXIBLE_TIME_FORMATTER)
        )

    val endDateTime: LocalDateTime
        get() = LocalDateTime.of(
            LocalDate.parse(date, DATE_FORMATTER),
            LocalTime.parse(endAt, FLEXIBLE_TIME_FORMATTER)
        )

    fun calculateAge(): Int {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val birthDate = LocalDate.parse(dateOfBirth, formatter)
        val today = LocalDate.now()

        return Period.between(birthDate, today).years
    }
}