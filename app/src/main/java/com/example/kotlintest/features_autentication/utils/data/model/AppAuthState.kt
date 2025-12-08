package com.example.kotlintest.features_autentication.utils.data.model

enum class AppAuthState(val type:Int) {
    FirstTime(1),
    UserNameChecked(2),
    LogedIn (3)
}