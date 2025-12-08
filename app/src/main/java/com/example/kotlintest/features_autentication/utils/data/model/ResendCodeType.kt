package com.example.kotlintest.features_autentication.utils.data.model

enum class ResendCodeType(val type:Int) {
    SignupVerification(1),
    ChangePhoneNumber(2),
    ResetPassword(3),
    ChangeEmail(4)
}