package com.example.kotlintest.features_autentication.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckOtpRequest(
    @SerialName("isTokenValid")

    var isTokenValid:Boolean = false
)