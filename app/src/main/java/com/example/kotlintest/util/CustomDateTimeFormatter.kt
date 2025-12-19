package com.example.kotlintest.util

import com.example.kotlintest.util.data.model.DateOfBirth
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object CustomDateTimeFormatter {

    /* -------------------- FORMATTERS -------------------- */

    private val fullDateFormatter =
        DateTimeFormatter.ofPattern("dd LLL, E", Locale.ENGLISH)

    private val dayFormatter =
        DateTimeFormatter.ofPattern("E", Locale.ENGLISH)

    private val timeAmPmFormatter =
        DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

    private val timeWithSecondsFormatter =
        DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSS", Locale.ENGLISH)

    /* -------------------- DATE FORMATS -------------------- */

    /** 11 DEC, THU */
    fun formatFullDate(isoDateTime: String): String =
        ZonedDateTime.parse(isoDateTime).format(fullDateFormatter)

    /** THU */
    fun formatDay(isoDateTime: String): String =
        (ZonedDateTime.parse(isoDateTime).format(dayFormatter)).uppercase()

    /* -------------------- TIME FORMATS -------------------- */

    /** 10:00 AM */
    fun formatTimeAmPm(
        isoDateTimeWithOffset: String,
        time: String
    ): String {
        val offset = OffsetDateTime.parse(isoDateTimeWithOffset).offset
        val localTime = LocalTime.parse(time)

        return localTime
            .atOffset(offset)
            .format(timeAmPmFormatter)
    }

    /** 19:52:48.1368880+03:00 */
    fun timeWithOffset(
        isoDateTimeWithOffset: String,
        time: String
    ): String {
        val offset = OffsetDateTime.parse(isoDateTimeWithOffset).offset
        val localTime = LocalTime.parse(time)

        return OffsetTime.of(localTime, offset).toString()
    }

    fun formatTimeAmPmWithOffset(
        dateTimeWithOffset: String,
        time: String
    ): String {
        // 1. Extract offset from first parameter
        val offset = OffsetDateTime.parse(dateTimeWithOffset).offset

        // 2. Parse time only
        val localTime = LocalTime.parse(time)

        // 3. Apply offset & format
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        return localTime.atOffset(offset).format(formatter)
    }

    /* -------------------- OFFSET -------------------- */

    /** +03:00 */
    fun extractOffset(isoDateTimeWithOffset: String): String =
        OffsetDateTime.parse(isoDateTimeWithOffset).offset.toString()


    fun String.convertToServerFormat():String{
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)  // Input format
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)  // Desired output format

        // Parse input string to Date object
        val dateObject: Date = inputFormat.parse(this)

        // Format Date object to desired output format
        return outputFormat.format(dateObject)

        return outputFormat.toString()
    }

    fun combineDateAndTimeToUtc(
        dateTimeWithOffset: String,
        timeOnly: String
    ): String {

        // Parse date + offset
        val offsetDateTime = OffsetDateTime.parse(dateTimeWithOffset)
        val date = offsetDateTime.toLocalDate()
        val offset = offsetDateTime.offset

        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

        //  Parse time only
        val localTime = LocalTime.parse(timeOnly)

        //  Combine date + time + offset
        val combined = OffsetDateTime.of(date, localTime, offset)

        //  Convert to UTC
        val utcDateTime = combined.withOffsetSameInstant(ZoneOffset.UTC)

        // Custom formatter
        val formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.ENGLISH
        )

        return utcDateTime.format(formatter)
    }

    fun getDateOnly(
        date : DateOfBirth
    ):String{
        return date.year + "-"+date.month + "-"+date.day
    }
}