package com.example.kotlintest.features_autentication.domain.usecase

import com.example.kotlintest.features_autentication.domain.model.ResetPasswordReq
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.util.data.BaseUseCase
import com.example.kotlintest.util.data.model.MainResponse
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthenticationRepository
): BaseUseCase<ResetPasswordReq, Unit>() {
    override suspend fun onCall(i: ResetPasswordReq): MainResponse<Unit> {
        return repository.resetPassword(i)
    }
}