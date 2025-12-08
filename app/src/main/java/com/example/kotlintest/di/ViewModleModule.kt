package com.example.kotlintest.di

import android.content.Context
import com.example.kotlintest.features_autentication.data.repository.AuthenticationRepositoryImpl
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.features_autentication.utils.data.remote.MedLinkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModleModule {
    @Provides
    @ViewModelScoped
    fun getAuthRepository(api: MedLinkApi): AuthenticationRepository {
        return AuthenticationRepositoryImpl(api)
    }


}