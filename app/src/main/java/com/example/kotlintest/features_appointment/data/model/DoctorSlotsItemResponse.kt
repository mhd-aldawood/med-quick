package com.example.kotlintest.features_appointment.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DoctorSlotsItemResponse(
    @SerialName("doctorId")
    var doctorId:Int= 0,

    @SerialName("doctorName")
    var doctorName:String="",

    @SerialName("doctorImageToken")
    var doctorImageToken:String="",

    @SerialName("doctorCharge")
    var doctorCharge:Int= 0,

    @SerialName("doctorChargeCurrency")
    var doctorChargeCurrency:String="",

    @SerialName("appointmentDurationInMinutes")
    var appointmentDurationInMinutes:Int= 0,

    @SerialName("slots")
    var slots:ArrayList<SlotItemResponse> = ArrayList<SlotItemResponse>()
)
