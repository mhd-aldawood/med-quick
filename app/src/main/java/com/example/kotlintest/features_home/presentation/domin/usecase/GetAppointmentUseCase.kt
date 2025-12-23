package com.example.kotlintest.features_home.presentation.domin.usecase

import com.example.kotlintest.features_home.presentation.data.model.AppointmentResponse
import com.example.kotlintest.features_home.presentation.domin.model.CreateCalendarReq
import com.example.kotlintest.features_home.presentation.domin.repository.CalendarRepository
import com.example.kotlintest.util.Const
import com.example.kotlintest.util.data.BaseUseCase
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import com.example.kotlintest.util.data.model.MainResponse
import javax.inject.Inject

class GetAppointmentUseCase @Inject constructor(
    private val sharedPreferanceRepository: SharedPreferanceRepository,
    private val repository: CalendarRepository
) : BaseUseCase<CreateCalendarReq, List<AppointmentResponse>>() {
    override suspend fun onCall(req: CreateCalendarReq): MainResponse<List<AppointmentResponse>> {
        var token = "Bearer " + sharedPreferanceRepository.getString(Const.TOKEN, "")
        return repository.getAppointmentList(token = token, req = req)
    }
}