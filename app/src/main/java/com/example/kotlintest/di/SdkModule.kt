package com.example.kotlintest.di

import android.content.Context
import com.example.kotlintest.core.model.DataUtilsFactory
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import serial.jni.BluConnectionStateListener
import serial.jni.DataUtils
import java.util.Timer
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)//TODO replace for ViewmodelComponent
object SdkModule {
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

    @Provides
    @Singleton
    fun provideAppTimer(): Timer {
        // daemon = true so it won't block JVM/app shutdown
        return Timer("app-timer", /* isDaemon = */ true)
    }


}