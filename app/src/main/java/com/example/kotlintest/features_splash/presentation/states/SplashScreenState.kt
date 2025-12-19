package com.example.kotlintest.features_splash.presentation.states

import com.example.kotlintest.util.data.model.AppAuthState

data class SplashScreenState(
    val appAuthState : AppAuthState = AppAuthState.Loading,
)
