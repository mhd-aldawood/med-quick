package com.example.kotlintest.features_appointment.presentation.events



sealed class AppointmentCreateUiEvent {
    data class ShowSnackBar(val message: String) : AppointmentCreateUiEvent()

    data class ShowSnackBarAndPop(val message: String) : AppointmentCreateUiEvent()
}