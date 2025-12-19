package com.example.kotlintest.features_appointment.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SpecialtyItemResponse(
    @SerialName("id")
    var id:Int= 0,
    @SerialName("name")
    var name:String="",
    @SerialName("imageToken")
    var imageToken:String="",
    @SerialName("availableDoctorsCount")
    var availableDoctorsCount:Int= 0
)
