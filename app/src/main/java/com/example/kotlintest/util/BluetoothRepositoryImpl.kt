package com.example.kotlintest.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    BluetoothRepository {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        manager.adapter
    }
    public override fun getAdapter(): BluetoothAdapter? {
        return bluetoothAdapter
    }

    override fun getPairedDevices(): Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices


    private  val TAG = "BluetoothRepositoryImpl"

    override fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    override fun stopScan(scanCallback: ScanCallback) {



        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
    }

    override fun startScan(permissionsList:List<String>,onDeviceFound: (BluetoothDevice, Int, ByteArray?) -> Unit) {

        val scanner = bluetoothAdapter?.bluetoothLeScanner ?: return
        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.let {
                    it.scanRecord?.deviceName?.let { message -> Logger.i(TAG,"device name "+message) }
                    onDeviceFound(it.device, it.rssi, it.scanRecord?.bytes)

                }
            }

            override fun onScanFailed(errorCode: Int) {
                Logger.e("BluetoothRepositoryImpl", "Scan failed: $errorCode")
            }
        }
        val allPermissionsGranted = permissionsList.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        if (allPermissionsGranted) {
            scanner.startScan(callback)
        } else {
            Logger.e("BluetoothRepositoryImpl", "No permission to scan bluetooth devices")
            return
        }

    }

}