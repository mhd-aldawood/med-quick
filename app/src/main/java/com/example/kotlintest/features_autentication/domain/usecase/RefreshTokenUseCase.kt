package com.example.kotlintest.features_autentication.domain.usecase

import com.example.kotlintest.features_autentication.data.model.LoginResponseDto
import com.example.kotlintest.features_autentication.domain.repository.RefeshTolkenRepository
import com.example.kotlintest.features_autentication.utils.Const
import com.example.kotlintest.features_autentication.utils.data.model.MainResources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(private val repository: RefeshTolkenRepository) {
    operator fun invoke(refresh_token: String): Flow<MainResources<LoginResponseDto>> = flow<MainResources<LoginResponseDto>> {
        try {
            emit(MainResources.isLoading())
            var response = repository.refreshToken(
                "refresh_token",
                Const.clientIdTest,
                "",
                "",
                Const.clientSecretTest,
                refresh_token
            )
            emit(MainResources.Sucess(data = response))
        } catch (e: HttpException) {
            emit(MainResources.isError(message = e.message() ?: ""))

        } catch (e: IOException) {
            emit(MainResources.isError(message = e.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

}