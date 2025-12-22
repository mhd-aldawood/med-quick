package com.example.kotlintest.features_autentication.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("loginResponse")
    var loginResponse: LoginResponseDto = LoginResponseDto(),
    @SerialName("user")
    var user: UserReqDto = UserReqDto(),
    @SerialName("isBasicInfoFilled")
    var isBasicInfoFilled:Boolean =false,
    @SerialName("userNameAvailability")
    var userNameAvailability: UserNameAvailabilityResponse = UserNameAvailabilityResponse(),
)