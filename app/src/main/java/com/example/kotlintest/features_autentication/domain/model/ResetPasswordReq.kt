package com.example.kotlintest.features_autentication.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordReq(
    @SerialName("phoneNumber")
    var email:String= "",
    @SerialName("token")
    var token:String = "",
    @SerialName("password")
    var password:String = "",
    @SerialName("confirmPassword")
    var confirmPassword:String= ""
)