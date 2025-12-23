package com.example.kotlintest.features_home.presentation.data.model

import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppointmentResponse (

    @SerialName("date"         ) var date         : String,
    @SerialName("appointments" ) var appointments : ArrayList<Appointments>

)
