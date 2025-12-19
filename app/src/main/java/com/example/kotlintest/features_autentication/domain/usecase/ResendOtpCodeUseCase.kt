package com.example.kotlintest.features_autentication.domain.usecase

import com.example.kotlintest.features_autentication.domain.model.ResendOtpCodeReq
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.util.data.BaseUseCase
import com.example.kotlintest.util.data.model.MainResponse
import javax.inject.Inject

class ResendOtpCodeUseCase @Inject constructor(
    private val repository: AuthenticationRepository
) : BaseUseCase<ResendOtpCodeReq, Unit>() {
    override suspend fun onCall(i: ResendOtpCodeReq): MainResponse<Unit> {
        return repository.resendCode(i)
    }
}