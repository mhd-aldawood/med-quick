package com.example.kotlintest.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback

interface BluetoothRepository {
    fun startScan(permission:List<String>,onDeviceFound: (BluetoothDevice, Int, ByteArray?) -> Unit)
    fun isBluetoothEnabled(): Boolean
    fun stopScan(scanCallback: ScanCallback)
    public fun getAdapter(): BluetoothAdapter?
    fun getPairedDevices(): Set<BluetoothDevice>?

}
