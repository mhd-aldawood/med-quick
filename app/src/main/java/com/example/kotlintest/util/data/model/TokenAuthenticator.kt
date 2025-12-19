package com.example.kotlintest.util.data.model

import android.content.Context
import android.content.Intent
import com.example.kotlintest.features_autentication.domain.usecase.RefreshTokenUseCase
import com.example.kotlintest.util.Const
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenRefresher: RefreshTokenUseCase,
    private val tokenProvider: SharedPreferanceRepository,
    private val context:Context
) : Authenticator {
    private val lock = Any()
    private var isRefreshing = false
    private var refreshFailed = false


    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = tokenProvider.getString(Const.REFRESH_TOKEN, "")
        synchronized(lock) {
            var currentAccessToken = tokenProvider.getString(Const.TOKEN, "")
            var currentRefreshToken  = tokenProvider.getString(Const.REFRESH_TOKEN, "")
                if (!isRefreshing && currentRefreshToken.equals(refreshToken)) {
                    isRefreshing = true
                    runBlocking {
                        tokenRefresher(refreshToken).collect { result ->
                            when (result) {
                                is MainResources.Sucess -> {
                                    // Save new tokens
                                    tokenProvider.saveString(
                                        Const.TOKEN,
                                        result.data?.access_token ?: ""
                                    )
                                    tokenProvider.saveString(
                                        Const.REFRESH_TOKEN,
                                        result.data?.refresh_token ?: ""
                                    )
                                    tokenProvider.saveInt(
                                        Const.EXPIRE_DATE,
                                        result.data?.expires_in ?: 0
                                    )
                                    currentAccessToken = result.data?.access_token ?: ""

                                }

                                is MainResources.isError -> {
                                    // Handle error
                                    tokenProvider.remove(Const.TOKEN)
                                    tokenProvider.remove(Const.REFRESH_TOKEN)
                                    tokenProvider.remove(Const.EXPIRE_DATE)
                                    tokenProvider.saveBoolean(Const.failedRefresh, true)
                                    var complelete = tokenProvider.getBoolean(Const.COMPLETE, false)
                                    restartApp()
                                    refreshFailed = true
                                }

                                else -> {
                                    // Handle other cases
                                }
                            }
                        }
                    }
                    isRefreshing = false
                    (lock as Object).notifyAll()
                } else {
                    currentAccessToken = tokenProvider.getString(Const.TOKEN, "")
                }
            if (refreshFailed) {
                return null // This cancels the waiting request
            }
            return currentAccessToken.let {
                response.request.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
            }
        }
    }
    fun restartApp() {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

}
