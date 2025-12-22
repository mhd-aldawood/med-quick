package com.example.kotlintest.features_autentication.presentation.events

import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.presentation.components.AuthCardScreen

sealed class AuthScreenEvent {
    class SaveSelectedProviderEvent(val provider: ClinicItemResponse)  : AuthScreenEvent()

    class ChangeAuthScreenCardEvent(val screenCard : AuthCardScreen) : AuthScreenEvent()

    class OnCheckUserNameChangedEvent(val checkUserName : String) : AuthScreenEvent()


    object CheckUserNameEvent : AuthScreenEvent()

    object ResetEvent : AuthScreenEvent()

    class CheckOtpCodeEvent(val otpCode : String) : AuthScreenEvent()

    object ResendOtpCodeEvent : AuthScreenEvent()

    class OnPasswordChangedEvent(val password : String) : AuthScreenEvent()

    class OnConfirmPasswordChangedEvent(val confirmPassword : String) : AuthScreenEvent()
    object OnResetPasswordClickEvent : AuthScreenEvent()

    class OnSignInUserNameChangedEvent(val signInUserName : String) : AuthScreenEvent()

    class OnSignInPasswordChangedEvent(val signInPassword : String) : AuthScreenEvent()

    object OnSignInClickEvent : AuthScreenEvent()
}