package com.example.kotlintest.features_autentication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClinicItemResponse(
    @SerialName("id")
    var id:Int = 0,
    @SerialName("name")
    var name:String ="",
    @SerialName("email")
    var email:String?= null,
    @SerialName("phoneNumber")
    var phoneNumber:String ="",
)