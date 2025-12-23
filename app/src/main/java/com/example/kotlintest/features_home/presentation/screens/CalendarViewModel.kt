package com.example.kotlintest.features_home.presentation.screens

import androidx.lifecycle.viewModelScope
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import com.example.kotlintest.core.model.CalendarCard
import com.example.kotlintest.core.model.CalendarCardStatus
import com.example.kotlintest.core.model.CalendarMoreCardModel
import com.example.kotlintest.features_home.presentation.data.model.AppointmentResponse
import com.example.kotlintest.features_home.presentation.data.model.Appointments
import com.example.kotlintest.features_home.presentation.domin.model.CreateCalendarReq
import com.example.kotlintest.features_home.presentation.domin.usecase.GetAppointmentUseCase
import com.example.kotlintest.util.Logger
import com.example.kotlintest.util.data.model.MainResources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val useCase: GetAppointmentUseCase) :
    BaseViewModel<CalendarState, CalendarEvents, CalendarActions>(
        initialState = CalendarState()
    ) {
    init {
        getAppointmentList()
    }

    private val TAG = "CalendarViewModel"
    private fun getAppointmentList() {
        val formatter = DateTimeFormatter.ofPattern("M-d-yyyy")

    // Current date
        val today = LocalDate.now()
        val todayFormatted = today.format(formatter)

    // Date after one week
        val afterOneWeek = today.plusWeeks(1)
        val afterOneWeekFormatted = afterOneWeek.format(formatter)
        useCase(
            CreateCalendarReq(
                fromDate = todayFormatted,
                toDate = afterOneWeekFormatted,
            )
        ).onEach {
            when (it) {
                is MainResources.Sucess<*> -> {
                    viewModelScope.launch(Dispatchers.Default) {
                        val cards = it.data.orEmpty().toCalendarCards()

                        mutableState.update {
                            it.copy(
                                appointmentsList = cards
                            )
                        }
                    }

                }

                is MainResources.isError<*> -> Logger.i(TAG, it.message.toString())
                is MainResources.isLoading<*> -> Logger.i(TAG, "Loading msg")
                is MainResources.isUnAuthorized<*> -> Logger.i(TAG, "isUnAuthorized")
            }
        }.launchIn(viewModelScope)
    }

    override fun handleAction(action: CalendarActions) {
        // TODO: handle actions
    }
}

sealed class CalendarActions {

}

sealed class CalendarEvents {

}

data class CalendarState(val appointmentsList: List<CalendarCard> = listOf())

fun List<AppointmentResponse>.toCalendarCards(
    maxVisible: Int = 3
): List<CalendarCard> {

    val result = mutableListOf<CalendarCard>()

    forEach { day ->
        result.addAll(day.toCalendarCards(maxVisible))
    }

    return result
}

fun AppointmentResponse.toCalendarCards(
    maxVisible: Int = 3
): List<CalendarCard> {

    val cards = mutableListOf<CalendarCard>()

    val visibleAppointments = appointments.take(maxVisible)
    val remainingCount = appointments.size - maxVisible

    visibleAppointments.forEach { appointment ->
        cards.add(appointment.toCalendarCard())
    }

    if (remainingCount > 0) {
        cards.add(
            CalendarMoreCardModel(
                count = remainingCount.toString(),
                calendarStatus = listOf(CalendarCardStatus.NotSelected)
            )
        )
    }

    return cards
}

fun Appointments.toCalendarCard(): CalendarAppointmentCardModel {
    val startDateTime = LocalDateTime.parse(time)

// Formatter for date only (as you want)
    val dateFormatter = DateTimeFormatter.ofPattern("M-d-yyyy")

// Start date (formatted)
    val startDateFormatted = startDateTime.format(dateFormatter)

// End date = start + 20 minutes
    val endDateTime = startDateTime.plusMinutes(20)
    val endDateFormatted = endDateTime.format(dateFormatter)
    return CalendarAppointmentCardModel(
        id = id.toString(),
        patientName = patient.fullName,
        locationText = address.getFullLocation(),
        nurseName = nurseName,
        kitName = kitSerial,
        date = startDateFormatted,
        age = patient.dateOfBirth,
        gender = patient.getGender(),
        selected = getStatus(),
        calendarStatus = listOf(CalendarCardStatus.NotSelected),
        endDate =endDateFormatted
    )
}
