package com.example.kotlintest.features_home.presentation.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Appointments (

    @SerialName("id"                ) var id                : Int,
    @SerialName("patient"           ) var patient           : Patient,
    @SerialName("status"            ) var status            : Int,
    @SerialName("address"           ) var address           : Address?,
    @SerialName("time"              ) var time              : String,
    @SerialName("durationInMinutes" ) var durationInMinutes : Int,
    @SerialName("kitSerial"         ) var kitSerial         : String?,
    @SerialName("nurseName") var nurseName           : String,
){
    fun getStatus(): Boolean=when(status){
        1->true
        else->false
    }
}
