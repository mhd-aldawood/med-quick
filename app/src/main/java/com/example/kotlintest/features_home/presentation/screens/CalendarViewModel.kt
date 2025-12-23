package com.example.kotlintest.features_home.presentation.screens

import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import com.example.kotlintest.features_home.presentation.domin.model.AppointmentListResponse
import com.example.kotlintest.features_home.presentation.domin.model.Appointments
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() :
    BaseViewModel<CalendarState, CalendarEvents, CalendarActions>(
        initialState = CalendarState()
    ) {

    override fun handleAction(action: CalendarActions) {
        // TODO: handle actions
    }
}

sealed class CalendarActions {

}

sealed class CalendarEvents {

}

data class CalendarState(val appointmentsList:List<CalendarAppointmentCardModel> =listOf())
