package com.example.kotlintest.features_autentication.domain.usecase

import com.example.kotlintest.features_autentication.data.model.UserNameAvailabilityResponse
import com.example.kotlintest.features_autentication.domain.model.CheckUserNameReq
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.util.data.BaseUseCase
import com.example.kotlintest.util.data.model.MainResponse
import javax.inject.Inject

class CheckUserNameUseCase @Inject constructor(
    private val repos: AuthenticationRepository
) : BaseUseCase<CheckUserNameReq, UserNameAvailabilityResponse>(){
    override suspend fun onCall(i: CheckUserNameReq): MainResponse<UserNameAvailabilityResponse> {
        return repos.checkUserNameAvailable (i.userName)
    }
}