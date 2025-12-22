package com.example.kotlintest.features_autentication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserNameAvailabilityResponse(
    @SerialName("isAvailable")
    var isAvailable:Boolean=false,
    @SerialName("isAccountActive")
    var isAccountActive:Boolean=false
)
