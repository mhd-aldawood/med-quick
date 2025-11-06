package com.example.kotlintest

import android.app.Application
import com.kl.minttisdk.ble.BleManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        BleManager.getInstance().init(this)

    }
}
