package com.example.kotlintest.di

import android.content.Context
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.audio.AudioProcessor
import com.example.kotlintest.core.bluetooth.WbcBleDevice
import com.example.kotlintest.core.workers.BloodAnalyzerWorker
import com.example.kotlintest.core.workers.PoctWorker
import com.example.kotlintest.core.workers.PulseOximeterWorker
import com.example.kotlintest.core.workers.StethoScopeWorker
import com.example.kotlintest.core.workers.ThermometerWorker
import com.example.kotlintest.core.workers.TonometerWorker
import com.example.kotlintest.core.workers.Worker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Timer

@Module
@InstallIn(ViewModelComponent::class)
object WorkerModule {
    @TonometerQualifier
    @Provides
    fun provideTonometerWorker(
        @ApplicationContext context: Context,
        sdk: com.contec.bp.code.connect.ContecSdk,
        deviceManager: DeviceManager
    ): Worker = TonometerWorker(
        context = context,
        sdk = sdk,
        deviceManager = deviceManager,
    )

    @PulseOximeterQualifier
    @Provides
    fun providePulseOximeterWorker(
        @ApplicationContext context: Context,
        sdk: com.contec.spo2.code.connect.ContecSdk,
        timer: Timer, deviceManager: DeviceManager
    ): Worker = PulseOximeterWorker(
        context = context,
        sdk = sdk,
        timer = timer,
        deviceManager = deviceManager
    )

    @StethoScopeQualifier
    @Provides
    fun provideStethoScopeWorker(
        deviceManager: DeviceManager,
        audioProcessor: AudioProcessor
    ): StethoScopeWorker = StethoScopeWorker(
        deviceManager = deviceManager,
        audioProcessor = audioProcessor
    )

    @ThermometerQualifier
    @Provides
    fun provideThermometerWorker(
        deviceManager: DeviceManager,
        sdk: com.contec.htd.code.connect.ContecSdk
    ): Worker = ThermometerWorker(
        deviceManager = deviceManager,
        sdk = sdk
    )

    @BloodAnalyzerQualifier
    @Provides
    fun provideBloodAnalyzerWorker(
        deviceManager: DeviceManager,
        wbcBleDevice: WbcBleDevice
    ): Worker = BloodAnalyzerWorker(deviceManager = deviceManager, wbcDevice = wbcBleDevice)
    @Provides
    fun providePoctWorker(): PoctWorker = PoctWorker()
}