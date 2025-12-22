package com.example.kotlintest.features_autentication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("access_token")
    var access_token:String="",
    @SerialName("expires_in")
    var expires_in:Int= 0,
    @SerialName("token_type")
    var token_type:String="",
    @SerialName("refresh_token")
    var refresh_token:String="",
    @SerialName("scope")
    var scope:String=""
)