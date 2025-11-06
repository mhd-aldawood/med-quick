package com.example.kotlintest.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build

class BluetoothScanner(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        manager.adapter
    }
    private val foundDevices = mutableListOf<BluetoothDevice>()
    private var onFinished: ((List<Pair<String, String>>) -> Unit)? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(
                                BluetoothDevice.EXTRA_DEVICE,
                                BluetoothDevice::class.java
                            )
                        } else {
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }
                    device?.let {
                        val name = it.name ?: "Unknown"
                        val mac = it.address
                        println("Found: $name ($mac)")
                        foundDevices.add(it)
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    println("Discovery finished with ${foundDevices.size} devices.")
                    context.unregisterReceiver(this)
                    // Call the callback when done
                    val result = foundDevices.map { it.name.orEmpty() to it.address }
                    if (result.isNotEmpty())
                        onFinished?.invoke(result)
                }
            }
        }
    }

    fun stopDiscovery() {
        bluetoothAdapter?.let {
            if (it.isDiscovering) {
                it.cancelDiscovery()
                println("Bluetooth discovery stopped.")
            }
        }

        // Also unregister the BroadcastReceiver if you registered it
        try {
            context.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            // Receiver already unregistered — ignore
        }
    }

    fun startDiscovery(onFinished: (List<Pair<String, String>>) -> Unit) {
        this.onFinished = onFinished
        foundDevices.clear()

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }

        context.registerReceiver(receiver, filter)
        bluetoothAdapter?.startDiscovery()
        println("Scanning started…")
    }
}
