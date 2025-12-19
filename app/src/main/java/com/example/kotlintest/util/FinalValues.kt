package com.example.kotlintest.util

import com.google.android.recaptcha.RecaptchaClient

open class FinalValues {
    companion object{
        var deviceToken = ""
        var recaptchaClient:RecaptchaClient? =null
    }

}