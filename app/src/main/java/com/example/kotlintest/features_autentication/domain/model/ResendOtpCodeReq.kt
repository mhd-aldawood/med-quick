package com.example.kotlintest.features_autentication.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResendOtpCodeReq(
    @SerialName("userName")

    var userName:String="",
    @SerialName("codeType")

    var codeType:Int= 0
)
