package com.example.kotlintest.features_autentication.data.repository

import com.example.kotlintest.features_autentication.data.model.LoginResponseDto
import com.example.kotlintest.features_autentication.domain.repository.RefeshTolkenRepository
import com.example.kotlintest.features_autentication.utils.data.remote.TokenApi
import javax.inject.Inject

class RefeshTolkenRepositoryImpl @Inject constructor(private val api: TokenApi):
    RefeshTolkenRepository {
    override suspend fun refreshToken(
        grant_type: String,
        client_id: String,
        username: String,
        password: String,
        client_secret: String,
        refresh_token: String
    ): LoginResponseDto {
        return api.refresh(grant_type,client_id,username, password, client_secret, refresh_token)
    }
}