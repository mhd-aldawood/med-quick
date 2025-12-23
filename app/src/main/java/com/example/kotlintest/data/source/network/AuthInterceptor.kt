package com.example.kotlintest.data.source.network

import com.example.kotlintest.util.Const
import com.example.kotlintest.util.Logger
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sharedPreferanceRepository: SharedPreferanceRepository) : Interceptor {
    private val TAG = "AuthInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authToken =sharedPreferanceRepository.getString(Const.TOKEN,"")
        Logger.i(TAG,authToken)
        // Add the authorization header to the request
        val requestWithHeader = originalRequest.newBuilder()
            .header("Authorization", "Bearer $authToken")
            .build()

        // Proceed with the new request
        return chain.proceed(requestWithHeader)
    }
}