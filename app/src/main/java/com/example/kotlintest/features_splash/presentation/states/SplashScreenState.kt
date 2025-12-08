package com.example.kotlintest.features_splash.presentation.states

import com.example.kotlintest.features_autentication.data.model.CheckOtpRequest
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.data.model.LoginResponse
import com.example.kotlintest.features_autentication.data.model.UserNameAvailabilityResponse
import com.example.kotlintest.features_autentication.domain.model.CheckUserNameReq
import com.example.kotlintest.features_autentication.presentation.components.AuthCardScreen
import com.example.kotlintest.features_autentication.utils.data.model.AppAuthState
import com.example.kotlintest.features_autentication.utils.data.model.RequestStates

data class SplashScreenState(
    val appAuthState : AppAuthState = AppAuthState.FirstTime,
)
