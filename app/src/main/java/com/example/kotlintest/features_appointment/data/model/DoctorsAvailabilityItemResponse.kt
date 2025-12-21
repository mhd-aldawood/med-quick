package com.example.kotlintest.features_appointment.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DoctorsAvailabilityItemResponse (
    @SerialName("schedules")
    var schedules:ArrayList<ScheduleItemResponse> = ArrayList<ScheduleItemResponse>()
)



