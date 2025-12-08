package com.example.kotlintest.features_autentication.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginReq(
    @SerialName("userName")
    var userName: String = "",

    @SerialName("password")
    var password: String = "",

    @SerialName("clientId")
    var clientId:String ="",

    @SerialName("clientSecret")
    var clientSecret:String="",

    @SerialName("platform")
    var platform:Int= 0,

    @SerialName("fcmToken")
    var fcmToken:String ="",
)