package com.example.kotlintest.core.model

class TimeEvent(val startTimeMillis: Long, val endTimeMillis: Long) {
    fun calculateTimeDifference(): String {
        val diffInMillis = this.endTimeMillis - this.startTimeMillis

        val hours = diffInMillis / 3600000
        val minutes = (diffInMillis % 3600000) / 60000
        val seconds = (diffInMillis % 60000) / 1000

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}

