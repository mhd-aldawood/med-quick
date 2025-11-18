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

    // Callbacks
    private var onFinishedList: ((List<Pair<String, String>>) -> Unit)? = null
    private var onFoundSingle: ((BluetoothDevice) -> Unit)? = null

    // Target matcher
    private var targetMatcher: ((String?) -> Boolean)? = null

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
                            @Suppress("DEPRECATION")
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        }

                    device?.let { dev ->
                        val name = dev.name ?: "Unknown"
                        val mac = dev.address
                        println("Found: $name ($mac)")
                        foundDevices.add(dev)

                        // If a target matcher is set and matches, stop immediately and return the device
                        targetMatcher?.let { match ->
                            if (match(name)) {
                                onFoundSingle?.invoke(dev)
                                safelyStopDiscoveryAndUnregister()
                                // prevent list callback on finish path
                                onFinishedList = null
                            }
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    println("Discovery finished with ${foundDevices.size} devices.")
                    onFinishedList?.let { cb ->
                        val result = foundDevices.map { it.name.orEmpty() to it.address }
                        if (result.isNotEmpty()) cb(result)
                    }
                    safelyUnregisterReceiver()
                    clearCallbacks()
                }
            }
        }
    }

    /** Public: stop discovery if running, and unregister receiver */
    fun stopDiscovery() {
        bluetoothAdapter?.let {
            if (it.isDiscovering) {
                it.cancelDiscovery()
                println("Bluetooth discovery stopped.")
            }
        }
        safelyUnregisterReceiver()
        clearCallbacks()
    }

    /** Original API: discover and return the full list at the end */
    fun startDiscovery(onFinished: (List<Pair<String, String>>) -> Unit) {
        this.onFinishedList = onFinished
        this.onFoundSingle = null
        this.targetMatcher = null
        internalStartDiscovery()
    }

    /**
     * New API: discover until a device name matches `targetNameMatcher`.
     * On first match, stop discovery and invoke `onFound` with the device.
     */
    fun startDiscovery(
        targetNameMatcher: (String?) -> Boolean,
        onFound: (BluetoothDevice) -> Unit
    ) {
        this.onFinishedList = null
        this.onFoundSingle = onFound
        this.targetMatcher = targetNameMatcher
        internalStartDiscovery()
    }

    /**
     * Convenience: exact name match (case-insensitive).
     */
    fun startDiscoveryExactName(
        targetName: String,
        onFound: (BluetoothDevice) -> Unit
    ) {
        startDiscovery(
            targetNameMatcher = { name -> name?.equals(targetName, ignoreCase = true) == true },
            onFound = onFound
        )
    }

    /**
     * >>> The one you asked for: accept a list of names and stop on first match. <<<
     * Exact match by default, case-insensitive.
     */
    private val TAG = "BlutoothScanner"
    fun startDiscovery(
        targetNames: List<String>,
        ignoreCase: Boolean = true,
        onFound: (BluetoothDevice) -> Unit,
    ) {
        val normalizedPrefixes =
            if (ignoreCase) targetNames.map { it.lowercase() } else targetNames
        startDiscovery(
            targetNameMatcher = { n ->
                val name = n ?: return@startDiscovery false
                val normalizedName = if (ignoreCase) name.lowercase() else name
                normalizedPrefixes.any { prefix -> normalizedName.startsWith(prefix) }
            },
            onFound = onFound
        )
    }

    // Internal start
    private fun internalStartDiscovery() {
        foundDevices.clear()

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        context.registerReceiver(receiver, filter)

        val started = bluetoothAdapter?.startDiscovery() == true
        println(if (started) "Scanning started…" else "Failed to start discovery.")
        if (!started) {
            safelyUnregisterReceiver()
            clearCallbacks()
        }
    }

    private fun safelyStopDiscoveryAndUnregister() {
        bluetoothAdapter?.let {
            if (it.isDiscovering) it.cancelDiscovery()
        }
        safelyUnregisterReceiver()
        clearCallbacks()
    }

    private fun safelyUnregisterReceiver() {
        try {
            context.unregisterReceiver(receiver)
        } catch (_: IllegalArgumentException) {
            // Receiver already unregistered — ignore
        }
    }

    private fun clearCallbacks() {
        onFinishedList = null
        onFoundSingle = null
        targetMatcher = null
    }

    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true
}
