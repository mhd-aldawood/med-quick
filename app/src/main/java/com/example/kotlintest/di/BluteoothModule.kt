package com.example.kotlintest.di

import android.content.Context
import com.example.kotlintest.core.bluetooth.BleUartDevice
import com.example.kotlintest.core.bluetooth.BluetoothScanner
import com.example.kotlintest.core.bluetooth.WbcBleDevice
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@dagger.Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {
    @Singleton
    @Provides
    fun provideBluetoothRepository(@ApplicationContext context_: Context): BluetoothScanner =
        BluetoothScanner(context_)

    @Singleton
    @Provides
    fun provideBleUartDevice(@ApplicationContext context_: Context): BleUartDevice =
        BleUartDevice(context_)

    @Singleton
    @Provides
    fun provideWbcBleDevice(bleUartDevice: BleUartDevice): WbcBleDevice =
        WbcBleDevice(bleUartDevice)


}
