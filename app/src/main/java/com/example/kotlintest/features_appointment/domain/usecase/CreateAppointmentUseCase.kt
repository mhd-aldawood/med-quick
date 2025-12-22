package com.example.kotlintest.features_appointment.domain.usecase

import com.example.kotlintest.features_appointment.data.model.CreateAppointmentResponse
import com.example.kotlintest.features_appointment.domain.model.CreateAppointmentReq
import com.example.kotlintest.features_appointment.domain.repository.AppointmentRepository
import com.example.kotlintest.util.Const
import com.example.kotlintest.util.data.BaseUseCase
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import com.example.kotlintest.util.data.model.MainResponse
import javax.inject.Inject

class CreateAppointmentUseCase @Inject constructor(
    private val sharedPreferanceRepository: SharedPreferanceRepository,
    private val repository: AppointmentRepository
): BaseUseCase<CreateAppointmentReq, CreateAppointmentResponse>(){
    override suspend fun onCall(i: CreateAppointmentReq): MainResponse<CreateAppointmentResponse> {
        var token = "Bearer "+sharedPreferanceRepository.getString(Const.TOKEN,"")
        var lang = Const.getLangCode(sharedPreferanceRepository.getString(Const.LANG,"en"))
        return repository.createAppointment(token,lang,i)
    }
}