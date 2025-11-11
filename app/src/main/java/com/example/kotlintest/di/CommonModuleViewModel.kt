package com.example.kotlintest.di

import android.content.Context
import com.example.kotlintest.core.audio.AudioProcessor
import com.example.kotlintest.core.audio.PcmFileReader
import com.example.kotlintest.core.audio.PcmPlayer
import com.example.kotlintest.core.model.DataUtilsFactory
import com.example.kotlintest.util.AuscultationRecordLoader
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import serial.jni.BluConnectionStateListener
import serial.jni.DataUtils

@dagger.Module
@InstallIn(ViewModelComponent::class)
object CommonModuleViewModel {
    @Provides
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
    fun providePcmPlayer(): PcmPlayer = PcmPlayer()

    @Provides
    fun provideAuscultationRecordLoader(@ApplicationContext appContext: Context): AuscultationRecordLoader =
        AuscultationRecordLoader(appContext)

    @Provides
    fun providePcmFileReader(): PcmFileReader = PcmFileReader()

    @Provides
    fun provideAudioProcessor(
        @ApplicationContext context: Context,
        pcmPlayer: PcmPlayer,
        pcmFileReader: PcmFileReader
    ): AudioProcessor {
        return AudioProcessor(context, pcmPlayer, pcmFileReader)
    }


}