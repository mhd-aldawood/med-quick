package com.example.kotlintest.features_autentication.utils.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MainResponse<T>(
    @SerialName("data")
    var data:T?=null,
    @SerialName("isSuccess")
    var isSuccess: Boolean=false,
    @SerialName("hasException")
    var hasException:Boolean=false,
    @SerialName("status")
    var status:Int=0,
    @SerialName("message")
    var message:String="",
)