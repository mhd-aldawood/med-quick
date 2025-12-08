package com.example.kotlintest.features_autentication.domain.model

data class CheckOtpReq(
    var userName:String="",
    var otp:String=""
)