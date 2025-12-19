package com.example.kotlintest.features_autentication.domain.repository

import com.example.kotlintest.features_autentication.data.model.CheckOtpRequest
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.data.model.LoginResponse
import com.example.kotlintest.features_autentication.data.model.UserNameAvailabilityResponse
import com.example.kotlintest.features_autentication.domain.model.LoginReq
import com.example.kotlintest.features_autentication.domain.model.ResendOtpCodeReq
import com.example.kotlintest.features_autentication.domain.model.ResetPasswordReq
import com.example.kotlintest.util.data.model.MainResponse


interface AuthenticationRepository {
    suspend fun login(req: LoginReq): MainResponse<LoginResponse>
    //suspend fun checkOtp(phoneNumber: String,otp:String):MainResponse<CheckOtpRequest>
    suspend fun getClinicList():MainResponse<ArrayList<ClinicItemResponse>>

    suspend fun checkUserNameAvailable(userName: String):MainResponse<UserNameAvailabilityResponse>

    suspend fun checkOtp(userName: String,otp:String):MainResponse<CheckOtpRequest>

    suspend fun resendCode( req: ResendOtpCodeReq):MainResponse<Unit>

    suspend fun resetPassword(req: ResetPasswordReq):MainResponse<Unit>

}