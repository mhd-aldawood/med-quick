package com.example.kotlintest.features_autentication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    @SerialName("access_token")
    var accessToken:String="",
    @SerialName("expires_in")
    var expiresIn:Int= 0,
    @SerialName("token_type")
    var tokenType:String="",
    @SerialName("refresh_token")
    var refreshToken:String="",
    @SerialName("scope")
    var scope:String=""
)