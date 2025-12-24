package com.example.kotlintest.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import com.example.kotlintest.core.model.CalendarCardStatus
import com.example.kotlintest.core.model.CalendarCard
import com.example.kotlintest.core.model.CalendarMoreCardModel
import com.example.kotlintest.core.model.Gender
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.ui.unit.Dp
import com.example.kotlintest.core.model.PositionedAppointmentCardModel
import java.time.*
import kotlin.math.max
import kotlin.math.min

val AppointmentList: List<CalendarCard> = listOf(
    CalendarAppointmentCardModel(
        id = "0",
        patientName = "patientName",
        locationText = "locationText",
        nurseName = "nurseName",
        kitName = "kitName",
        date = "15:00:00",
        gender = Gender.Male,
        dateOfBirth = "age",
        calendarStatus = listOf(CalendarCardStatus.Notification, CalendarCardStatus.Selected),
        selected = false, startAt = "TODO()",
        endAt = "TODO()",
    ), CalendarAppointmentCardModel(
        id = "1",
        patientName = "patientName",
        locationText = "locationText",
        nurseName = "nurseName",
        kitName = "kitName",
        date = "15:16:14.6770000",
        gender = Gender.Male,
        dateOfBirth = "age",
        calendarStatus = listOf(CalendarCardStatus.Selected),
        selected = false,startAt = "TODO()",
        endAt = "TODO()",
    ), CalendarAppointmentCardModel(
        id = "2",
        patientName = "patientName",
        locationText = "locationText",
        nurseName = "nurseName",
        kitName = "kitName",
        date = "2:00 PM",
        gender = Gender.Male,
        dateOfBirth = "age",
        calendarStatus = listOf(CalendarCardStatus.Warning),
        selected = false, startAt = "TODO()",
        endAt = "TODO()",
    ), CalendarAppointmentCardModel(
        id = "3",
        patientName = "patientName",
        locationText = "locationText",
        nurseName = "nurseName",
        kitName = "kitName",
        date = "2:40 PM",
        gender = Gender.Female,
        dateOfBirth = "age",
        calendarStatus = listOf(CalendarCardStatus.NotSelected),
        selected = true,startAt = "TODO()",
        endAt = "TODO()",
    ),
    CalendarMoreCardModel(
        count = "4",
        calendarStatus = listOf(CalendarCardStatus.NotSelected, CalendarCardStatus.Warning)
    )
)

// ---------- Models ----------
data class CalendarEvent(
    val id: String,
    val eventName: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
private fun maxOverlapForDay(events: List<CalendarEvent>): Int {
    if (events.isEmpty()) return 1

    val points = mutableListOf<Pair<LocalDateTime, Int>>()

    events.forEach { event ->
        points += event.startDate to +1
        points += event.endDate to -1
    }

    var current = 0
    var max = 0

    points.sortedBy { it.first }.forEach { (_, delta) ->
        current += delta
        max = maxOf(max, current)
    }

    return max
}
private fun requiredSlotsForDay(events: List<CalendarEvent>): Int {
    return when (val overlap = maxOverlapForDay(events)) {
        0, 1 -> 1
        2 -> 2
        else -> 3
    }
}


// ---------- Public Composable ----------
@Composable
fun DynamicCalendar(
    endHour: Int,
    endDays: Int,
    events: List<CalendarEvent>,
    modifier: Modifier = Modifier
) {
    // ---- Layout constants ----
    val hourHeight = 130.dp
    val baseEventWidth = 180.dp           // ✅ width of ONE event slot/
    val timeColumnWidth = 64.dp
    val headerHeight = 48.dp
    val subSlotsPerHour = 3
    val maxVisibleLanes = 3

    // ---- Time range ----
    val now = LocalDateTime.now()
    val startHour = min(
        now.hour,
        events.minOfOrNull { it.startDate.hour } ?: now.hour
    )
    val safeEndHour = max(endHour, startHour)
    val hours = (startHour..safeEndHour).toList()
    val hourCount = hours.size

    // ---- Day range ----
    val startDay = LocalDate.now()
    val endDay = startDay.plusDays(endDays.toLong())
    val days = remember(startDay, endDay) {
        generateSequence(startDay) { it.plusDays(1) }
            .takeWhile { !it.isAfter(endDay) }
            .toList()
    }

    // ---- Grid start reference ----
    val gridStartDateTime = LocalDateTime.of(startDay, LocalTime.of(startHour, 0))
    val gridEndDateTime = LocalDateTime.of(startDay, LocalTime.of(safeEndHour, 59))

    // ---- Scroll states ----
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    // ---- Compute DAY WIDTHS dynamically (🔥 IMPORTANT) ----
    val dayWidths: List<Dp> = remember(events, days) {
        days.map { day ->
            val dayEvents = events.filter {
                it.startDate.toLocalDate() == day
            }
            val slots = requiredSlotsForDay(dayEvents)
            baseEventWidth * slots
        }
    }

    // ---- Precompute cumulative X offsets per day ----
    val dayXOffsets: List<Dp> = remember(dayWidths) {
        buildList {
            var acc = 0.dp
            dayWidths.forEach {
                add(acc)
                acc += it
            }
        }
    }

    val totalGridWidth = dayWidths.fold(0.dp) { acc, w -> acc + w }
    val gridHeight = hourHeight * hourCount

    // ---- Precompute positioned cards ----
    val positionedCards = remember(events, days, startHour, safeEndHour) {
        buildPositionedCards(
            days = days,
            gridStart = gridStartDateTime,
            gridEnd = gridEndDateTime,
            events = events,
            maxVisibleLanes = maxVisibleLanes
        )
    }

    Column(modifier = modifier.fillMaxSize()) {

        // ---------- HEADER ----------
        Row(modifier = Modifier.height(headerHeight)) {
            Spacer(modifier = Modifier.width(timeColumnWidth))

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .horizontalScroll(horizontalScroll)
            ) {
                days.forEachIndexed { index, day ->
                    Box(
                        modifier = Modifier
                            .width(dayWidths[index])
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${day.dayOfMonth}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // ---------- BODY ----------
        Row(modifier = Modifier.fillMaxSize()) {

            // Time column
            Column(
                modifier = Modifier
                    .width(timeColumnWidth)
                    .verticalScroll(verticalScroll)
            ) {
                hours.forEach { hour ->
                    Box(
                        modifier = Modifier.height(hourHeight),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = String.format("%02d:00", hour),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Grid + events
            Box(
                modifier = Modifier
                    .horizontalScroll(horizontalScroll)
                    .verticalScroll(verticalScroll)
            ) {

                // ----- GRID -----
                Canvas(
                    modifier = Modifier
                        .width(totalGridWidth)
                        .height(gridHeight)
                ) {
                    val hourPx = hourHeight.toPx()

                    // Vertical day boundaries (dynamic)
                    dayXOffsets.forEach { xDp ->
                        val x = xDp.toPx()
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1f
                        )
                    }
                    // Rightmost border
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1f
                    )

                    // Horizontal hour + sub-lines
                    for (h in 0..hourCount) {
                        val baseY = h * hourPx
                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, baseY),
                            end = Offset(size.width, baseY),
                            strokeWidth = 2f
                        )

                        if (h < hourCount) {
                            val sub = hourPx / subSlotsPerHour
                            for (i in 1 until subSlotsPerHour) {
                                val y = baseY + i * sub
                                drawLine(
                                    color = Color.LightGray,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = 1f
                                )
                            }
                        }
                    }
                }

                // ----- EVENTS -----
                positionedCards.forEach { card ->
//                    CalendarEventCard(
//                        card = card,
//                        dayWidth = dayWidths[card.dayIndex],
//                        dayXOffset = dayXOffsets[card.dayIndex],
//                        hourHeight = hourHeight,
//                        startHour = startHour,
//                        lanesMax = maxVisibleLanes
//                    )

                }
            }
        }
    }
}


// ---------- UI Card ----------
@Composable
private fun CalendarEventCard(
    card: PositionedAppointmentCardModel,
    dayWidth: Dp,
    dayXOffset: Dp,
    hourHeight: Dp,
    startHour: Int,
    lanesMax: Int
) {
    val lanes = min(card.lanesCount, lanesMax)
    val laneWidth = dayWidth / lanes

    val topDp =
        (card.topMinutesFromGridStart / 60f) * hourHeight.value

    val heightDp =
        (card.durationMinutes / 60f) * hourHeight.value

    Box(
        modifier = Modifier
            .offset(
                x = dayXOffset + laneWidth * card.laneIndex,
                y = topDp.dp
            )
            .width(laneWidth)
            .height(max(12f, heightDp).dp)
            .padding(3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (card.isMoreCard) Color.DarkGray else Color(0xFF2F6BFF))
            .padding(8.dp)
    ) {
        Text(
            text = if (card.isMoreCard) "+${card.moreCount}" else card.title,
            color = Color.White,
            maxLines = 2
        )
    }
}


// ---------- Layout Engine (the important part) ----------
private fun buildPositionedCards(
    days: List<LocalDate>,
    gridStart: LocalDateTime,
    gridEnd: LocalDateTime,
    events: List<CalendarEvent>,
    maxVisibleLanes: Int
): List<PositionedAppointmentCardModel> {
    val result = mutableListOf<PositionedAppointmentCardModel>()

    days.forEachIndexed { dayIndex, day ->
        // events that start on this day (you can change rule if you want cross-day splitting)
        val dayEvents = events
            .filter { it.startDate.toLocalDate() == day }
            .mapNotNull { e ->
                // Clamp to grid time window (optional)
                val s = maxOf(e.startDate, gridStart.with(day))
                val en = minOf(e.endDate, gridEnd.with(day).plusMinutes(1))
                if (!s.isBefore(en)) null else e.copy(startDate = s, endDate = en)
            }
            .sortedBy { it.startDate }

        // Make overlap groups for that day
        val groups = buildOverlapGroups(dayEvents)

        for (group in groups) {
            val groupStart = group.minOf { it.startDate }
            val groupEnd = group.maxOf { it.endDate }

            // If overlaps <= 2 → show 1 or 2 lanes
            // If overlaps >= 3 → show 3 lanes where lane 3 is "+N"
            val lanesCount = when {
                group.size <= 1 -> 1
                group.size == 2 -> 2
                else -> maxVisibleLanes
            }

            if (group.size <= 2) {
                group.forEachIndexed { idx, e ->
                    result += PositionedAppointmentCardModel(
                        id = e.id,
                        title = e.eventName,
                        dayIndex = dayIndex,
                        laneIndex = if (lanesCount == 1) 0 else idx,
                        lanesCount = lanesCount,
                        topMinutesFromGridStart = minutesFrom(gridStart.with(day), e.startDate),
                        durationMinutes = max(1, minutesFrom(e.startDate, e.endDate))
                    )
                }
            } else {
                // Place up to 2 real events, the rest into +N
                val placed = assignLanesGreedy(group, lanes = 2)
                val placedIds = placed.map { it.first.id }.toSet()
                val hiddenCount = group.size - placedIds.size

                // 2 real event lanes
                placed.forEach { (e, lane) ->
                    result += PositionedAppointmentCardModel(
                        id = e.id,
                        title = e.eventName,
                        dayIndex = dayIndex,
                        laneIndex = lane, // 0 or 1
                        lanesCount = lanesCount, // 3
                        topMinutesFromGridStart = minutesFrom(gridStart.with(day), e.startDate),
                        durationMinutes = max(1, minutesFrom(e.startDate, e.endDate))
                    )
                }

                // "+N" lane spans the overlap group time window
                result += PositionedAppointmentCardModel(
                    id = "more-${dayIndex}-${groupStart}",
                    title = "",
                    dayIndex = dayIndex,
                    laneIndex = 2,
                    lanesCount = lanesCount,
                    topMinutesFromGridStart = minutesFrom(gridStart.with(day), groupStart),
                    durationMinutes = max(1, minutesFrom(groupStart, groupEnd)),
                    isMoreCard = true,
                    moreCount = hiddenCount
                )
            }
        }
    }

    return result
}

private fun LocalDateTime.with(day: LocalDate): LocalDateTime =
    LocalDateTime.of(day, this.toLocalTime())

private fun minutesFrom(a: LocalDateTime, b: LocalDateTime): Int {
    return ((b.toEpochSecond(ZoneOffset.UTC) - a.toEpochSecond(ZoneOffset.UTC)) / 60).toInt()
}

// Build overlap groups (connected by time overlap)
private fun buildOverlapGroups(events: List<CalendarEvent>): List<List<CalendarEvent>> {
    if (events.isEmpty()) return emptyList()

    val sorted = events.sortedBy { it.startDate }
    val groups = mutableListOf<MutableList<CalendarEvent>>()

    var current = mutableListOf(sorted.first())
    var currentEnd = sorted.first().endDate

    for (i in 1 until sorted.size) {
        val e = sorted[i]
        if (e.startDate.isBefore(currentEnd)) {
            current.add(e)
            if (e.endDate.isAfter(currentEnd)) currentEnd = e.endDate
        } else {
            groups.add(current)
            current = mutableListOf(e)
            currentEnd = e.endDate
        }
    }

    groups.add(current)
    return groups
}

// Greedy lane assignment (returns only what fits into 'lanes' lanes)
private fun assignLanesGreedy(
    events: List<CalendarEvent>,
    lanes: Int
): List<Pair<CalendarEvent, Int>> {
    val sorted = events.sortedBy { it.startDate }
    val laneEnd = Array<LocalDateTime?>(lanes) { null }
    val placed = mutableListOf<Pair<CalendarEvent, Int>>()

    for (e in sorted) {
        var lane: Int? = null
        for (i in 0 until lanes) {
            val end = laneEnd[i]
            if (end == null || !e.startDate.isBefore(end)) {
                lane = i
                break
            }
        }
        if (lane != null) {
            laneEnd[lane] = e.endDate
            placed += e to lane
        }
    }
    return placed
}
fun calendarSampleEvents(): List<CalendarEvent> {
    val today = LocalDate.now()
    val now = LocalTime.now()

    return listOf(
        // 1:00 – 2:00 (full hour)
        CalendarEvent(
            id = "1",
            eventName = "music Event",
            startDate = today.atTime(now.hour, 0),
            endDate = today.atTime(now.hour+1, 0)
        ),

        // 1:00 – 1:20 (overlaps with long)
        CalendarEvent(
            id = "2",
            eventName = "dance event",
            startDate = today.atTime(now.hour, 0),
            endDate = today.atTime(now.hour, 20)
        ),

        // 1:20 – 1:40 (stacked under Short A)
        CalendarEvent(
            id = "3",
            eventName = "science Event",
            startDate = today.atTime(now.hour, 20),
            endDate = today.atTime(now.hour, 40)
        ),

        // 1:40 – 2:00 (stacked under Short B)
        CalendarEvent(
            id = "4",
            eventName = "bio event",
            startDate = today.atTime(now.hour, 40),
            endDate = today.atTime(now.hour+1, 0)
        ),

        // Extra overlap → should go into +N
        CalendarEvent(
            id = "5",
            eventName = "Extra Overlap",
            startDate = today.atTime(now.hour, 10),
            endDate = today.atTime(now.hour, 50)
        ),

        // Event in another hour
        CalendarEvent(
            id = "6",
            eventName = "Later Event",
            startDate = today.atTime(now.hour+2, 0),
            endDate = today.atTime(now.hour+3, 0)
        )
    )
}
@Preview
@Composable
fun CalendarScreen() {
    DynamicCalendar(
        endHour = 22,        // calendar ends at 10 PM
        endDays = 5,         // today + next 5 days
        events =   calendarSampleEvents() ,
        modifier = Modifier.fillMaxSize()
    )
}
