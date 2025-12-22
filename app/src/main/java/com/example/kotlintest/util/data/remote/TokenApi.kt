package com.example.kotlintest.util.data.remote

import com.example.kotlintest.features_autentication.data.model.LoginResponseDto
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenApi {
    @FormUrlEncoded
    @POST("token")
    suspend fun refresh(@Field("grant_type") grant_type:String,
                        @Field("client_id") client_id:String,
                        @Field("username") username:String,
                        @Field("password") password:String,
                        @Field("client_secret") client_secret:String,
                        @Field("refresh_token") refresh_token:String
    ): LoginResponseDto
}