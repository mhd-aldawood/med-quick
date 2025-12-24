package com.example.kotlintest.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import com.example.kotlintest.core.model.CalendarCardStatus
import com.example.kotlintest.core.model.Gender
import com.example.kotlintest.core.model.PositionedCard
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.min

private fun maxOverlapForDay(
    events: List<CalendarAppointmentCardModel>
): Int {
    if (events.isEmpty()) return 1

    val points = mutableListOf<Pair<LocalDateTime, Int>>()
    events.forEach {
        points += it.startDateTime to +1
        points += it.endDateTime to -1
    }

    var current = 0
    var max = 0

    points.sortedBy { it.first }.forEach {
        current += it.second
        max = maxOf(max, current)
    }

    return max
}

private fun requiredSlotsForDay(
    events: List<CalendarAppointmentCardModel>
): Int = when (maxOverlapForDay(events)) {
    0, 1 -> 1
    2 -> 2
    else -> 3
}
fun buildPositionedCards(
    days: List<LocalDate>,
    events: List<CalendarAppointmentCardModel>,
    startHour: Int,
    hourHeight: Dp,
    maxLanes: Int
): List<PositionedCard> {

    val result = mutableListOf<PositionedCard>()
    val hourPx = hourHeight.value

    days.forEachIndexed { dayIndex, day ->

        val dayEvents = events
            .filter { it.startDateTime.toLocalDate() == day }
            .sortedBy { it.startDateTime }

        val lanes = mutableListOf<MutableList<CalendarAppointmentCardModel>>()

        dayEvents.forEach { event ->
            var placed = false

            for (lane in lanes) {
                if (lane.last().endDateTime <= event.startDateTime) {
                    lane.add(event)
                    placed = true
                    break
                }
            }

            if (!placed && lanes.size < maxLanes) {
                lanes.add(mutableListOf(event))
            }
        }

        lanes.forEachIndexed { laneIndex, lane ->
            lane.forEach { event ->

                val startMinutes =
                    (event.startDateTime.hour - startHour) * 60 +
                            event.startDateTime.minute

                val durationMinutes =
                    Duration.between(
                        event.startDateTime,
                        event.endDateTime
                    ).toMinutes()

                result += PositionedCard(
                    model = event,
                    dayIndex = dayIndex,
                    lane = laneIndex,
                    top = (startMinutes / 60f * hourPx).dp,
                    height = (durationMinutes / 60f * hourPx).dp
                )
            }
        }
    }

    return result
}

@Composable
fun DynamicCalendarNew(
    endHour: Int,
    endDays: Int,
    events: List<CalendarAppointmentCardModel>,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit={} // ✅ ADD THIS
) {
    val hourHeight = 300.dp//130
    val baseEventWidth = 235.dp//180
    val timeColumnWidth = 64.dp
    val headerHeight = 48.dp
    val subSlots = 3
    val maxLanes = 3

    val now = LocalDateTime.now()
    val startHour = min(
        now.hour,
        events.minOfOrNull { it.startDateTime.hour } ?: now.hour
    )

    val startDay = LocalDate.now()
    val endDay = startDay.plusDays(endDays.toLong())

    val days = remember(startDay, endDay) {
        generateSequence(startDay) { it.plusDays(1) }
            .takeWhile { !it.isAfter(endDay) }
            .toList()
    }

    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    val dayWidths = remember(events) {
        days.map { day ->
            baseEventWidth * requiredSlotsForDay(
                events.filter { it.startDateTime.toLocalDate() == day }
            )
        }
    }

    val dayXOffsets = remember(dayWidths) {
        buildList {
            var acc = 0.dp
            dayWidths.forEach {
                add(acc)
                acc += it
            }
        }
    }

    val totalWidth = dayWidths.fold(0.dp) { a, b -> a + b }
    val gridHeight = hourHeight * (endHour - startHour + 1)

    val positionedCards = remember(events) {
        buildPositionedCards(
            days, events, startHour, hourHeight, maxLanes
        )
    }
    Column(modifier.fillMaxSize()) {

        // HEADER
        Row(Modifier.height(headerHeight)) {
            Spacer(Modifier.width(timeColumnWidth))
            Row(Modifier.horizontalScroll(horizontalScroll)) {
                days.forEachIndexed { i, day ->
                    Box(
                        Modifier.width(dayWidths[i]),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day.dayOfMonth.toString(), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Row {

            // TIME COLUMN
            Column(
                Modifier
                    .width(timeColumnWidth)
                    .verticalScroll(verticalScroll)
            ) {
                (startHour..endHour).forEach {
                    Box(
                        Modifier.height(hourHeight),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text("%02d:00".format(it))
                    }
                }
            }

            // GRID + EVENTS
            Box(
                Modifier
                    .horizontalScroll(horizontalScroll)
                    .verticalScroll(verticalScroll)
            ) {

                Canvas(
                    Modifier
                        .width(totalWidth)
                        .height(gridHeight)
                )
                {
                    val hourPx = hourHeight.toPx()

                    dayXOffsets.forEach {
                        val x = it.toPx()
                        drawLine(
                            Color.LightGray,
                            Offset(x, 0f),

                            Offset(x, size.height),
                            1f
                        )
                    }

                    drawLine(
                        Color.LightGray,
                        Offset(size.width, 0f),
                        Offset(size.width, size.height),
                        1f
                    )

                    for (h in 0..(endHour - startHour)) {
                        val y = h * hourPx
                        drawLine(
                            Color.Gray,
                            Offset(0f, y),
                            Offset(size.width, y),
                            2f
                        )

                        val sub = hourPx / subSlots
                        for (i in 1 until subSlots) {
                            drawLine(
                                Color.LightGray,
                                Offset(0f, y + i * sub),
                                Offset(size.width, y + i * sub),
                                1f
                            )
                        }
                    }
                }

                positionedCards.forEach { card ->
                    CalendarAppointmentCard(
                        card = card.model,
                        modifier = Modifier
                            .offset(
                                x = dayXOffsets[card.dayIndex] +
                                        baseEventWidth * card.lane,
                                y = card.top
                            ).clickable{
                                onCardClick(card.model.id)
                            }
                    )
                }
            }
        }
    }
}


fun calendarAppointmentsSample(): List<CalendarAppointmentCardModel> {

    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("M-d-yyyy")

    fun day(offset: Long) =
        today.plusDays(offset).format(dateFormatter)

    return listOf(

        // ---------- TODAY ----------
        CalendarAppointmentCardModel(
            id = "1",
            patientName = "Ahmed Ali",
            locationText = "Dubai Marina",
            nurseName = "Nurse Sara",
            kitName = "KIT-001",
            date = day(0),
            startAt = "13:00:00",
            endAt = "14:00:00",
            dateOfBirth = "32",
            gender = Gender.Male,
            selected = false,
            calendarStatus = listOf(CalendarCardStatus.NotSelected)
        ),

        CalendarAppointmentCardModel(
            id = "2",
            patientName = "Mona Hassan",
            locationText = "JBR",
            nurseName = "Nurse Lina",
            kitName = "KIT-002",
            date = day(0),
            startAt = "13:10:00",
            endAt = "13:30:00",
            dateOfBirth = "28",
            gender = Gender.Female,
            selected = false,
            calendarStatus = listOf(CalendarCardStatus.NotSelected)
        ),

        CalendarAppointmentCardModel(
            id = "3",
            patientName = "Yousef Omar",
            locationText = "Business Bay",
            nurseName = "Nurse Adam",
            kitName = "KIT-003",
            date = day(0),
            startAt = "13:20:00",
            endAt = "13:50:00",
            dateOfBirth = "40",
            gender = Gender.Male,
            selected = false,
            calendarStatus = listOf(CalendarCardStatus.NotSelected)
        ),

        // ---------- TOMORROW ----------
        CalendarAppointmentCardModel(
            id = "4",
            patientName = "Fatima Noor",
            locationText = "Downtown",
            nurseName = "Nurse Aya",
            kitName = "KIT-004",
            date = day(1),
            startAt = "09:00:00",
            endAt = "09:20:00",
            dateOfBirth = "35",
            gender = Gender.Female,
            selected = true,
            calendarStatus = listOf(CalendarCardStatus.NotSelected)
        ),

        CalendarAppointmentCardModel(
            id = "5",
            patientName = "Omar Khaled",
            locationText = "Al Barsha",
            nurseName = "Nurse Sam",
            kitName = "KIT-005",
            date = day(1),
            startAt = "09:10:00",
            endAt = "10:00:00",
            dateOfBirth = "45",
            gender = Gender.Male,
            selected = false,
            calendarStatus = listOf(CalendarCardStatus.NotSelected)
        ),

        // ---------- DAY AFTER ----------
        CalendarAppointmentCardModel(
            id = "6",
            patientName = "Layla Zaid",
            locationText = "Palm Jumeirah",
            nurseName = "Nurse Noor",
            kitName = "KIT-006",
            date = day(2),
            startAt = "15:00:00.6770000", // ✅ milliseconds supported
            endAt = "15:20:00",
            dateOfBirth = "29",
            gender = Gender.Female,
            selected = false,
            calendarStatus = listOf(CalendarCardStatus.NotSelected)
        )
    )
}
@Preview
@Composable
fun CalendarNewScreenNew() {
    DynamicCalendarNew(
        endHour = 22,
        endDays = 5,
        events = calendarAppointmentsSample(),
        modifier = Modifier.fillMaxSize()
    )
}