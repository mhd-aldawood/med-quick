package com.example.kotlintest.features_home.presentation.screens

import androidx.lifecycle.viewModelScope
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import com.example.kotlintest.core.model.CalendarCardStatus
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
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
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
            when(action){
                is CalendarActions.OnCardClicked -> handleOnCardClicked(action.id)
                is CalendarActions.BtnClicked -> {
                    sendEvent(CalendarEvents.NavigateToExamination(action.appointmentId))
                }
            }
    }

    private fun handleOnCardClicked(id: String) {
        mutableState.update { it ->
            it.copy(selectedCard=stateFlow.value.appointmentsList.find { it.id == id })
        }
    }
}

sealed class CalendarActions() {
    data class BtnClicked(val appointmentId: String) : CalendarActions()

    data class OnCardClicked(val id: String) : CalendarActions()
}

sealed class CalendarEvents {
    data class NavigateToExamination(val appointmentsId: String) : CalendarEvents()

}

data class CalendarState(val appointmentsList: List<CalendarAppointmentCardModel> = listOf(),val selectedCard:CalendarAppointmentCardModel?=null)

fun List<AppointmentResponse>.toCalendarCards(
    maxVisible: Int = 3
): List<CalendarAppointmentCardModel> {

    val result = mutableListOf<CalendarAppointmentCardModel>()

    forEach { day ->
        result.addAll(day.toCalendarCards(maxVisible,day.date))
    }

    return result
}

fun AppointmentResponse.toCalendarCards(
    maxVisible: Int = 3,
    date1: String
): List<CalendarAppointmentCardModel> {

    val cards = mutableListOf<CalendarAppointmentCardModel>()

    val visibleAppointments = appointments.take(maxVisible)
    val remainingCount = appointments.size - maxVisible
    appointments.forEach { appointment ->
        cards.add(appointment.toCalendarCard(date1))
    }
//    visibleAppointments.forEach { appointment ->
//        cards.add(appointment.toCalendarCard(date1))
//    }
//this part is for the more card disabled temporary
//    if (remainingCount > 0) {
//        cards.add(
//            CalendarMoreCardModel(
//                count = remainingCount.toString(),
//                calendarStatus = listOf(CalendarCardStatus.NotSelected)
//            )
//        )
//    }

    return cards
}

fun Appointments.toCalendarCard(parentDate: String): CalendarAppointmentCardModel {

    val flexibleTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("H:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
        .optionalEnd()
        .toFormatter()
    val dateFormatter = DateTimeFormatter.ofPattern("M-d-yyyy")
    val outputTimeFormatter = DateTimeFormatter.ofPattern("H:mm:ss")

    val parentLocalDate = LocalDateTime
        .parse(parentDate) // "2025-12-25T00:00:00"
        .toLocalDate()

// ✅ Works with or without milliseconds
    val startTime = LocalTime.parse(time, flexibleTimeFormatter)

    val startDateTime = LocalDateTime.of(
        parentLocalDate,
        startTime
    )

// ➕ add 20 minutes
    val endDateTime = startDateTime.plusMinutes(20)
//    val endDateTime = startDateTime.plusMinutes(durationInMinutes.toLong())

// ✅ Drop milliseconds in output
    val endAt = endDateTime
        .toLocalTime()
        .format(outputTimeFormatter)

    return CalendarAppointmentCardModel(
        id = id.toString(),//appointmentId
        patientName = patient.fullName,
        locationText = address?.getFullLocation() ?: "Any Location Here",
        nurseName = nurseName,
        kitName = kitSerial ?: "Kit Name",
        date = parentLocalDate.format(dateFormatter),//12-23-2025
        endAt = endAt,//12-23-2025
        startAt = startDateTime.toLocalTime()
            .format(outputTimeFormatter),
        dateOfBirth = patient.dateOfBirth,
        gender = patient.getGender(),
        selected = getStatus(),
        calendarStatus = listOf(CalendarCardStatus.NotSelected),
    )
}
