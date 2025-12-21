package com.example.kotlintest.core.model

data class PositionedAppointmentCardModel(
    val id: String,
    val title: String,
    val dayIndex: Int,
    val laneIndex: Int,
    val lanesCount: Int, // 1..3
    val topMinutesFromGridStart: Int,
    val durationMinutes: Int,
    val isMoreCard: Boolean = false,
    val moreCount: Int = 0
)