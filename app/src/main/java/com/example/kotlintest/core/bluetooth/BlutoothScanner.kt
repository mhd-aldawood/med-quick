package com.example.kotlintest.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.example.kotlintest.util.Logger

class BluetoothScanner(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        manager.adapter
    }

    private val foundDevices = mutableListOf<BluetoothDevice>()

    // Callbacks
    private var onFoundSingle: ((BluetoothDevice) -> Unit)? = null

    // Retry control
    private val handler = Handler(Looper.getMainLooper())
    private var retryDelayMillis: Long = 0L
    private var maxRetries: Int? = null
    private var retryCount = 0
    private var retryEnabled = false

    // Target matcher
    private var targetMatcher: ((String?) -> Boolean)? = null
    private val TAG = "BlutoothScanner"
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
                        foundDevices.add(dev)

                        // Target match?
                        targetMatcher?.let { match ->
                            if (match(name)) {
                                retryEnabled = false
                                handler.removeCallbacksAndMessages(null)

                                onFoundSingle?.invoke(dev)
                                safelyStopDiscoveryAndUnregister()
                                return
                            }
                        }
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Logger.i(TAG, "Scan finished. Found ${foundDevices.size}. No match.")

                    if (retryEnabled) {
                        retryCount++
                        val allowed = maxRetries?.let { retryCount <= it } ?: true

                        if (allowed) {
                            Logger.i(TAG, "Retrying in $retryDelayMillis ms (attempt $retryCount)")
                            foundDevices.clear()

                            handler.postDelayed({
                                startScanInternal()
                            }, retryDelayMillis)

                            return
                        } else {
                            Logger.i(TAG, "Max retries reached. Stopping.")
                        }
                    }

                    safelyUnregisterReceiver()
                    clearCallbacks()
                }
            }
        }
    }

    /** Public stop */
    fun stopDiscovery() {
        retryEnabled = false
        handler.removeCallbacksAndMessages(null)

        bluetoothAdapter?.let {
            if (it.isDiscovering) it.cancelDiscovery()
        }
        safelyUnregisterReceiver()
        clearCallbacks()
    }

    /**
     * Start discovery with target matcher + retry logic.
     */
    fun startDiscovery(
        targetNameMatcher: (String?) -> Boolean,
        retryDelayMillis: Long = 0L,
        maxRetries: Int? = null,
        onFound: (BluetoothDevice) -> Unit
    ) {
        this.onFoundSingle = onFound
        this.targetMatcher = targetNameMatcher

        this.retryDelayMillis = retryDelayMillis
        this.maxRetries = maxRetries
        this.retryEnabled = retryDelayMillis > 0
        this.retryCount = 0

        startScanInternal()
    }

    /** Convenience: list of prefixes */
    fun startDiscovery(
        targetNames: List<String>,
        ignoreCase: Boolean = true,
        retryDelayMillis: Long = 0L,
        maxRetries: Int? = null,
        onFound: (BluetoothDevice) -> Unit,
    ) {
        val normalizedPrefixes =
            if (ignoreCase) targetNames.map { it.lowercase() } else targetNames

        startDiscovery(
            targetNameMatcher = { name ->
                val n = name ?: return@startDiscovery false
                val nn = if (ignoreCase) n.lowercase() else n
                normalizedPrefixes.any { nn.startsWith(it) }
            },
            retryDelayMillis = retryDelayMillis,
            maxRetries = maxRetries,
            onFound = onFound
        )
    }

    // Internal scan start
    private fun startScanInternal() {
        foundDevices.clear()

        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }

        context.registerReceiver(receiver, filter)

        val started = bluetoothAdapter?.startDiscovery() == true
        Logger.i(TAG, if (started) "Bluetooth scan started…" else "Failed to start scan")

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
        }
    }

    private fun clearCallbacks() {
        onFoundSingle = null
        targetMatcher = null
        retryEnabled = false
        retryDelayMillis = 0
        retryCount = 0
    }

    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true
}
