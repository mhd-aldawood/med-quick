package com.example.kotlintest.features_autentication.data.repository

import com.example.kotlintest.features_autentication.data.model.CheckOtpRequest
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.data.model.LoginResponse
import com.example.kotlintest.features_autentication.data.model.UserNameAvailabilityResponse
import com.example.kotlintest.features_autentication.domain.model.LoginReq
import com.example.kotlintest.features_autentication.domain.model.ResendOtpCodeReq
import com.example.kotlintest.features_autentication.domain.model.ResetPasswordReq
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.util.data.model.MainResponse
import com.example.kotlintest.util.data.remote.MedLinkApi
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val api: MedLinkApi
): AuthenticationRepository {
    override suspend fun login(req: LoginReq): MainResponse<LoginResponse> {
        return api.login(req)
    }
    override suspend fun getClinicList(): MainResponse<ArrayList<ClinicItemResponse>> {
        return api.getClinicsList()
    }
    override suspend fun checkUserNameAvailable(userName: String): MainResponse<UserNameAvailabilityResponse> {
        return api.checkUserNameAvailable (userName)
    }
    override suspend fun checkOtp(userName: String, otp: String): MainResponse<CheckOtpRequest> {
        return api.checkOtp(userName,otp)
    }

    override suspend fun resendCode(req: ResendOtpCodeReq, ): MainResponse<Unit> {
        return api.resendCode( req)
    }

    override suspend fun resetPassword(req: ResetPasswordReq): MainResponse<Unit> {
        return api.resetPassword(req)
    }

}