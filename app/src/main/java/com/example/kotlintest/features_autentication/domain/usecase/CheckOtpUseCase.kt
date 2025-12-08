package com.example.kotlintest.features_autentication.domain.usecase

import com.example.kotlintest.features_autentication.data.model.CheckOtpRequest
import com.example.kotlintest.features_autentication.domain.model.CheckOtpReq
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.features_autentication.utils.data.BaseUseCase
import com.example.kotlintest.features_autentication.utils.data.model.MainResponse
import javax.inject.Inject

class CheckOtpUseCase @Inject constructor(
    private val repos: AuthenticationRepository
) : BaseUseCase<CheckOtpReq, CheckOtpRequest>(){
    override suspend fun onCall(i: CheckOtpReq): MainResponse<CheckOtpRequest> {
        return repos.checkOtp(i.userName,i.otp)
    }
}