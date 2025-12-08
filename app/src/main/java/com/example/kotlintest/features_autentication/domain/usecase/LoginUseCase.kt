package com.example.kotlintest.features_autentication.domain.usecase

import com.example.kotlintest.features_autentication.data.model.LoginResponse
import com.example.kotlintest.features_autentication.domain.model.LoginReq
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.features_autentication.utils.FinalValues
import com.example.kotlintest.features_autentication.utils.data.BaseUseCase
import com.example.kotlintest.features_autentication.utils.data.model.MainResponse
import com.example.kotlintest.features_autentication.utils.receiveFCMToken
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: AuthenticationRepository
): BaseUseCase<LoginReq, LoginResponse>() {
    override suspend fun onCall(i: LoginReq): MainResponse<LoginResponse> {
//        if(FinalValues.deviceToken.isNullOrEmpty()){
//            receiveFCMToken()
//        }
//        i.fcmToken = FinalValues.deviceToken
        return repo.login(i)
    }
}