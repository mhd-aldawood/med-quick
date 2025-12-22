package com.example.kotlintest.features_autentication.domain.repository


import com.example.kotlintest.features_autentication.data.model.LoginResponseDto
import retrofit2.http.Field

interface RefeshTolkenRepository {
   suspend fun refreshToken(grant_type:String, client_id:String, username:String, password:String, client_secret:String, refresh_token:String): LoginResponseDto
}