package com.example.kotlintest.features_appointment.domain.usecase

import com.example.kotlintest.features_appointment.data.model.SpecialtiesResponse
import com.example.kotlintest.features_appointment.domain.repository.AppointmentRepository
import com.example.kotlintest.util.Const
import com.example.kotlintest.util.data.BaseUseCase
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import com.example.kotlintest.util.data.model.MainResponse
import javax.inject.Inject

class GetSpecialtiesUseCase @Inject constructor(
    private val sharedPreferanceRepository: SharedPreferanceRepository,
    private val repository: AppointmentRepository
) : BaseUseCase<Int, SpecialtiesResponse>() {
    override suspend fun onCall(i: Int): MainResponse<SpecialtiesResponse> {
        var token = "Bearer "+sharedPreferanceRepository.getString(Const.TOKEN,"")
        var lang = Const.getLangCode(sharedPreferanceRepository.getString(Const.LANG,"en"))
        return repository.getSpecialties(token,lang,i)
    }
}