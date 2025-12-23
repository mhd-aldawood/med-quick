package com.example.kotlintest.di

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kotlintest.data.source.network.AuthInterceptor
import com.example.kotlintest.features_autentication.data.repository.RefeshTolkenRepositoryImpl
import com.example.kotlintest.features_autentication.domain.repository.RefeshTolkenRepository
import com.example.kotlintest.features_autentication.domain.usecase.RefreshTokenUseCase
import com.example.kotlintest.util.Const
import com.example.kotlintest.util.data.local.SharedPreferanceRepository
import com.example.kotlintest.util.data.local.SharedPreferanceRepositoryImpl
import com.example.kotlintest.util.data.model.TokenAuthenticator
import com.example.kotlintest.util.data.remote.MedLinkApi
import com.example.kotlintest.util.data.remote.TokenApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Objects
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun getMasterKey(@ApplicationContext appContext: Context): MasterKey? {
        try {
            val spec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
                "_androidx_security_master_key_",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            return MasterKey.Builder(appContext)
                .setKeyGenParameterSpec(spec)
                .build()
        } catch (e: Exception) {
        }
        return null
    }


    @Provides
    @Singleton
    fun authenticator(useCase: RefreshTokenUseCase, repository: SharedPreferanceRepository, @ApplicationContext appContext: Context):TokenAuthenticator{
        return TokenAuthenticator(useCase, repository, context = appContext)
    }
    @Provides
    @Singleton
    fun getHttpClient(authenticator: TokenAuthenticator,authInterceptor:AuthInterceptor): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
       interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(authInterceptor)
        httpClient.addInterceptor(interceptor)
        httpClient.authenticator(authenticator)
        httpClient.callTimeout(Const.CALL_TIMEOUT.toLong(), TimeUnit.MINUTES)
            .connectTimeout(Const.CONNECT_TIMEOUT.toLong(), TimeUnit.MINUTES)
            .readTimeout(Const.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Const.WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        return httpClient.build()
    }
    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPreferanceRepository: SharedPreferanceRepository): AuthInterceptor {
        return AuthInterceptor(sharedPreferanceRepository)
    }

    @Provides
    @Singleton
    fun getMedLinkApi(httpClient: OkHttpClient): MedLinkApi {
        return Retrofit.Builder()
        .baseUrl(Const.url)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
        .create(MedLinkApi::class.java)
    }
    @Provides
    @Singleton
    fun getTokenApi(): TokenApi {
        return Retrofit.Builder()
            .baseUrl(Const.refreshUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenApi::class.java)
    }


    @Provides
    @Singleton
    fun getRefreshTokenRepository(api: TokenApi): RefeshTolkenRepository {
        return RefeshTolkenRepositoryImpl(api)
    }
    @Provides
    @Singleton
    fun getRefreshTokenUseCase(repository: RefeshTolkenRepository):RefreshTokenUseCase{
        return RefreshTokenUseCase(repository)
    }
    @Provides
    @Singleton
    fun getPrefs(@ApplicationContext appContext: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            Objects.requireNonNull(appContext),
            "medlink",
            getMasterKey(appContext)!!,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    @Provides
    @Singleton
    inline fun provideGson(): Gson {
        return GsonBuilder().create()
    }
    @Provides
    @Singleton
    inline fun provideSharedPreferanceRepository(preferance:SharedPreferences,gson: Gson): SharedPreferanceRepository {
        return SharedPreferanceRepositoryImpl(preferance, gson)
    }
}