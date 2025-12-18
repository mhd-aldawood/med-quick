package com.example.kotlintest.di

import android.content.Context
import com.contec.htd.code.connect.ContecSdk
import com.example.kotlintest.core.bluetooth.BluetoothRepository
import com.example.kotlintest.core.bluetooth.BluetoothRepositoryImpl
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@dagger.Module
@InstallIn(ViewModelComponent::class)
object SdkModuleViewModel {

    @Provides
    fun providePulseOximeterContecSdk(
        @ApplicationContext context: Context
    ): com.contec.spo2.code.connect.ContecSdk = com.contec.spo2.code.connect.ContecSdk(context)


    @Provides
    fun provideBluetoothRepository(
        @ApplicationContext context: Context
    ): BluetoothRepository {
        return BluetoothRepositoryImpl(context)
    }


    @Provides
    fun provideThermometerContecSdk(
        @ApplicationContext context: Context
    ): ContecSdk {
        return ContecSdk(context)
    }


    @Provides
    fun provideTonometerContecSdk(
    ): com.contec.bp.code.connect.ContecSdk = com.contec.bp.code.connect.ContecSdk()

    @Provides
    fun provideUrineAnalzyerContecSdk(
        @ApplicationContext
        context: Context
    ): com.contec.bc.code.connect.ContecSdk = com.contec.bc.code.connect.ContecSdk(context)

}