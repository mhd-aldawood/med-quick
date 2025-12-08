package com.example.kotlintest.features_autentication.utils

import android.content.Context
import com.google.android.recaptcha.RecaptchaClient

open class FinalValues {
    companion object{
        var deviceToken = ""
        var recaptchaClient:RecaptchaClient? =null
    }

}