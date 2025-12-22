package com.example.kotlintest.features_appointment.domain.usecase

import com.example.kotlintest.features_appointment.data.model.DoctorsAvailabilityItemResponse
import com.example.kotlintest.features_appointment.domain.model.DoctorsAvailabilityReq
import com.example.kotlintest.features_appointment.domain.repository.AppointmentRepository
import com.example.kotlintest.util.Const
import com.example.kotlintest.util.data.BaseUseCase
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import com.example.kotlintest.util.data.model.MainResponse
import javax.inject.Inject

class GetDoctorsAvailabilityUseCase @Inject constructor(
    private val sharedPreferanceRepository: SharedPreferanceRepository,
    private val repository: AppointmentRepository
) : BaseUseCase<DoctorsAvailabilityReq, DoctorsAvailabilityItemResponse>() {
    override suspend fun onCall(i: DoctorsAvailabilityReq): MainResponse<DoctorsAvailabilityItemResponse> {
        var token = "Bearer "+sharedPreferanceRepository.getString(Const.TOKEN,"")
        var lang = Const.getLangCode(sharedPreferanceRepository.getString(Const.LANG,"en"))
        return repository.getDoctorsSoonestAvailabilityList(token,lang,i.specialityId, i.forecastedDaysCount)
    }
}