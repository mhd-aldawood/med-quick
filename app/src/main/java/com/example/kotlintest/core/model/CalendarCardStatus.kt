package com.example.kotlintest.core.model

import com.example.kotlintest.R

enum class CalendarCardStatus(val icon:Int){
    Selected(icon= R.drawable.ic_card_check)
    ,Notification(icon=R.drawable.ic_bill)
    ,NotSelected(icon=R.drawable.ic_ring)
    ,Warning(icon = R.drawable.ic_warning)
}
