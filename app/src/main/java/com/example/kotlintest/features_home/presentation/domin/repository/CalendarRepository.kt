package com.example.kotlintest.features_home.presentation.domin.repository

import com.example.kotlintest.features_home.presentation.data.model.AppointmentResponse
import com.example.kotlintest.features_home.presentation.domin.model.CreateCalendarReq
import com.example.kotlintest.util.data.model.MainResponse

interface CalendarRepository {
    suspend fun getAppointmentList(token: String, req: CreateCalendarReq): MainResponse<List<AppointmentResponse>>
}