package com.example.kotlintest.features_home.presentation.domin.model

import com.example.kotlintest.core.model.CalendarAppointmentCardModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AppointmentListResponse (

    @SerialName("date"         ) var date         : String,
    @SerialName("appointments" ) var appointments : ArrayList<Appointments>

){
    fun getCalendarAppointmentCardModel():List<CalendarAppointmentCardModel> {
        val result = mutableListOf<CalendarAppointmentCardModel>()
//        appointments.forEach {
//            result.add(
//                CalendarAppointmentCardModel(
//                    id =it.id.toString(),
//                    patientName = it.patient.fullName,
//                    locationText = it.address.getFullLocation(),
//                    nurseName = it.patient.,
//                    kitName = TODO(),
//                    date = TODO(),
//                    age = TODO(),
//                    gender = TODO(),
//                    selected = TODO(),
//                    calendarStatus = TODO()
//                )
//            )
//        }
        return result
    }
}