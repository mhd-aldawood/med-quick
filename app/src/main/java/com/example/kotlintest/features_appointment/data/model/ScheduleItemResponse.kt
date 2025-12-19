package com.example.kotlintest.features_appointment.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ScheduleItemResponse(
    @SerialName("scheduleDate")
    var scheduleDate:String= "",

    @SerialName("doctors")
    var doctors:ArrayList<DoctorSlotsItemResponse> = ArrayList<DoctorSlotsItemResponse>()
)
