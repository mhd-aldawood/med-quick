package com.example.kotlintest.features_autentication.presentation.components

 sealed class AuthCardScreen {
    object ProviderScreen : AuthCardScreen()

    object CheckUserNameScreen : AuthCardScreen()
    object OtpScreen : AuthCardScreen()
    object SignInScreen : AuthCardScreen()
    object ResetPasswordScreen: AuthCardScreen()
    object HomeScreen: AuthCardScreen()


}