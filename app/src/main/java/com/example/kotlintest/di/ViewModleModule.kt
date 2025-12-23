package com.example.kotlintest.di

import com.example.kotlintest.features_autentication.data.repository.AuthenticationRepositoryImpl
import com.example.kotlintest.features_autentication.domain.repository.AuthenticationRepository
import com.example.kotlintest.features_appointment.data.repository.AppointmentRepositoryImpl
import com.example.kotlintest.features_appointment.domain.repository.AppointmentRepository
import com.example.kotlintest.features_home.presentation.data.repository.CalendarRepositoryImpl
import com.example.kotlintest.features_home.presentation.domin.repository.CalendarRepository
import com.example.kotlintest.util.data.remote.MedLinkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModleModule {
    @Provides
    @ViewModelScoped
    fun getAuthRepository(api: MedLinkApi): AuthenticationRepository {
        return AuthenticationRepositoryImpl(api)
    }

    @Provides
    @ViewModelScoped
    fun getAppointmentRepository(api: MedLinkApi): AppointmentRepository {
        return AppointmentRepositoryImpl(api)
    }

    @Provides
    @ViewModelScoped
    fun getCalendarRepository(api: MedLinkApi): CalendarRepository {
        return CalendarRepositoryImpl(api)
    }


}