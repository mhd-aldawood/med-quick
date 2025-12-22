package com.example.kotlintest.features_appointment.domain.model

data class DoctorsAvailabilityReq(
    var specialityId:Int=0,
    var forecastedDaysCount:Int=0,
)