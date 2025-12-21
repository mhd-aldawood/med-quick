package com.example.kotlintest.features_splash.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlintest.util.Const
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import com.example.kotlintest.util.data.model.AppAuthState
import com.example.kotlintest.features_splash.presentation.states.SplashScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sharedPreferanceRepository: SharedPreferanceRepository
): ViewModel() {

    private val _splashScreenState = MutableStateFlow(SplashScreenState())
    val splashScreenState: StateFlow<SplashScreenState> = _splashScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500)              // simulate loading
            //sharedPreferanceRepository.getBoolean(Const.FIRST_TIME,false)


            _splashScreenState.update { currentState ->

                currentState.copy(
                    appAuthState = sharedPreferanceRepository.getObjectFromSharedPreferences<AppAuthState>(Const.App_Auth_State,
                        AppAuthState::class.java)?:AppAuthState.FirstTime
                )
            }

        }
    }
}