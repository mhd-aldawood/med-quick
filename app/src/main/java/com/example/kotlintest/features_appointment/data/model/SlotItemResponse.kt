package com.example.kotlintest.features_appointment.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SlotItemResponse(
    @SerialName("startTime")
    var startTime:String="",

    @SerialName("endTime")
    var endTime:String="",

    @SerialName("appointmentDurationInMinutes")
    var appointmentDurationInMinutes:Int= 0
)
