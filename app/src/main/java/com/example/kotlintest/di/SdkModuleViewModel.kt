package com.example.kotlintest.di

import android.content.Context
import com.example.kotlintest.screens.ecg.model.ReviewWaveController
import com.example.kotlintest.screens.ecg.model.ReviewWaveControllerImpl
import com.example.kotlintest.util.BluetoothRepository
import com.example.kotlintest.util.BluetoothRepositoryImpl
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@dagger.Module
@InstallIn(ViewModelComponent::class)//TODO replace for ViewmodelComponent
object SdkModuleViewModel{

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

}