package com.example.kotlintest.di

import android.content.Context
import com.contec.htd.code.connect.ContecSdk
import com.example.kotlintest.core.model.DataUtilsFactory
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import serial.jni.BluConnectionStateListener
import serial.jni.DataUtils
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)//TODO replace for ViewmodelComponent
object SdkModule {
//    @Singleton
//    @Provides
//    fun provideThermometerContecSdk(
//        @ApplicationContext context: Context
//    ): ContecSdk {
//        return ContecSdk(context)
//    }

//    @Singleton
//    @Provides
//    fun providePulseOximeterContecSdk(
//        @ApplicationContext context: Context
//    ): com.contec.spo2.code.connect.ContecSdk = com.contec.spo2.code.connect.ContecSdk(context)


    @Singleton
    @Provides
    fun provideTonometerContecSdk(//TODO move to activitycomponenet
    ): com.contec.bp.code.connect.ContecSdk = com.contec.bp.code.connect.ContecSdk()

    @Provides
    @Singleton
    fun provideDataUtilsFactory(
        @ApplicationContext appContext: Context
    ): DataUtilsFactory = object : DataUtilsFactory {
        override fun create(
            address: String,
            lead: Int,
            debug: Boolean,
            listener: BluConnectionStateListener
        ): DataUtils {
            return DataUtils(
                appContext,
                address,
                lead,
                debug,
                listener
            )
        }
    }

    @Provides
    @Singleton
    fun provideConcurrentLinkedQueue(): ConcurrentLinkedQueue<Short> = ConcurrentLinkedQueue<Short>()
}