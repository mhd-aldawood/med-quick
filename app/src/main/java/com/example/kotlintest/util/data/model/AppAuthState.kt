package com.example.kotlintest.util.data.model

enum class AppAuthState(val type:Int) {
    Loading (0),
    FirstTime(1),
    UserNameChecked(2),
    LogedIn (3)


}