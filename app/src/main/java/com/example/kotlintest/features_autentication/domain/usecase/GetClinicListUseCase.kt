package com.example.kotlintest.features_autentication.domain.usecase

import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.util.data.BaseUseCase
import com.example.kotlintest.util.data.model.MainResponse
import javax.inject.Inject

class GetClinicListUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) : BaseUseCase<Unit, ArrayList<ClinicItemResponse>>(){
    override suspend fun onCall(i: Unit): MainResponse<ArrayList<ClinicItemResponse>> {
        return repository.getClinicList()
    }
}