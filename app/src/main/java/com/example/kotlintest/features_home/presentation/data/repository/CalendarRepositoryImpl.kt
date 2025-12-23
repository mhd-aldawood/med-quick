package com.example.kotlintest.features_home.presentation.data.repository

import com.example.kotlintest.features_home.presentation.data.model.AppointmentResponse
import com.example.kotlintest.features_home.presentation.domin.model.CreateCalendarReq
import com.example.kotlintest.features_home.presentation.domin.repository.CalendarRepository
import com.example.kotlintest.util.data.model.MainResponse
import com.example.kotlintest.util.data.remote.MedLinkApi
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val api:MedLinkApi
): CalendarRepository {
    override suspend fun getAppointmentList(
        token: String,
        req: CreateCalendarReq
    ): MainResponse<List<AppointmentResponse>> {
        return api.getCalendar(token, fromDate = req.fromDate, toDate = req.toDate, status = req.status?.code, culture = req.culture, onlyMe = req.onlyMe, serialNumber = req.serialNumber)
    }

}