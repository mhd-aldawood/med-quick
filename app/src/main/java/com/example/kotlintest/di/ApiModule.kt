package com.example.kotlintest.di

import android.content.Context
import com.contec.htd.code.connect.ContecSdk
import com.example.kotlintest.BuildConfig
import com.example.kotlintest.data.source.network.ApiService
import com.example.kotlintest.data.source.network.ApiServiceImpl
import com.example.kotlintest.util.BluetoothRepository
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Singleton
import com.example.kotlintest.util.BluetoothRepositoryImpl


@dagger.Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(DefaultRequest) {
                url(BuildConfig.BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header("x-api-key", BuildConfig.API_KEY)
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true

                })
            }

        }
    }

    @Singleton
    @Provides
    fun provideApiService(httpClient: HttpClient): ApiService = ApiServiceImpl(httpClient)


    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default




}