package com.example.kotlintest.features_autentication.presentation.states

import com.example.kotlintest.features_autentication.data.model.CheckOtpRequest
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.data.model.LoginResponse
import com.example.kotlintest.features_autentication.data.model.UserNameAvailabilityResponse
import com.example.kotlintest.features_autentication.domain.model.CheckUserNameReq
import com.example.kotlintest.features_autentication.presentation.components.AuthCardScreen
import com.example.kotlintest.features_autentication.utils.data.model.RequestStates

data class AuthScreenState(
    val providerList : RequestStates<ArrayList<ClinicItemResponse>> =  RequestStates(data = ArrayList()),
    val selectedProvider: ClinicItemResponse = ClinicItemResponse(),
    val screen : AuthCardScreen = AuthCardScreen.ProviderScreen,
    val checkUserName:String = "",
    val checkOtpRequest:RequestStates<CheckOtpRequest> = RequestStates(data = CheckOtpRequest()),
    val resendOtpCodeReq: RequestStates<Unit> = RequestStates(data = Unit),
    val otpCode:String = "",
    val password:String = "",
    val confirmPassword:String = "",
    val resetPasswordReq: RequestStates<Unit> = RequestStates(data = Unit),
    val signInUserName:String = "",
    val signInPassword:String = "",
    val signInRequset: RequestStates<LoginResponse> = RequestStates(data = LoginResponse()),
//    val isUserNameAvailable : Boolean = false,
//    val isAccountActive : Boolean = false,
    val checkUserNameObj: RequestStates<UserNameAvailabilityResponse> = RequestStates(data = UserNameAvailabilityResponse()),
    val isLoading: Boolean = false

)
