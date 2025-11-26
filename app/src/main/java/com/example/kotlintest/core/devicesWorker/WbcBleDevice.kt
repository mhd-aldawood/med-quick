package com.example.kotlintest.core.devicesWorker

import android.bluetooth.BluetoothDevice
import com.example.kotlintest.util.CellResult
import com.example.kotlintest.util.Logger
import com.example.kotlintest.util.parseCellResult
import com.example.kotlintest.util.parseWbcFrame
import com.example.kotlintest.util.wbcClearDataFrame
import com.example.kotlintest.util.wbcGetResultsFrame
import com.example.kotlintest.util.wbcHandshakeFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WbcBleDevice @Inject constructor(
    private val ble: BleUartDevice
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _latestResult = MutableStateFlow<CellResult?>(null)
    val latestResult: StateFlow<CellResult?> = _latestResult.asStateFlow()

    private val _connected = MutableStateFlow(false)
    val connected: StateFlow<Boolean> = _connected.asStateFlow()

    fun init(bluetoothDevice: BluetoothDevice) {
        ble.init(bluetoothDevice)
    }


    fun connect(result: (CellResult) -> Unit) {
        ble.connect()

        scope.launch {
            ble.framesFlow.collect { bytes ->
                // Log raw bytes if you like
                Logger.d("WBC", "RX: " + bytes.joinToString(" ") { "%02X".format(it) })

                val frame = try {
                    parseWbcFrame(bytes)
                } catch (e: IllegalArgumentException) {
                    Logger.e("WBC", "Failed to parse frame: ${e.message}")
                    return@collect      // just skip this packet
                }

                when (frame.op1) {
                    0x43 -> {
                        // Handshake report
                        // op1=0x43, op2='O', op3='N' usually
                        // You can mark handshake OK here if you want
                    }

                    0x24 -> {
                        // Report results
                        val result = parseCellResult(frame.payload)
                        result(result)
                        _latestResult.value = result
                    }

                    0x21 -> {
                        // Clear-data confirmation etc.
                    }

                    0x23 -> {
                        // Error code in frame.payload
                    }
                }
            }
        }

        // You can also hook connection state from BleUartDevice if you expose it.
        _connected.value = true
    }

    fun disconnect() {
        _connected.value = false
        ble.disconnect()
        scope.cancel()
    }

    fun requestHandshake() {
        ble.sendFrame(wbcHandshakeFrame())
    }

    fun requestLatestResult() {
        // Many devices expect handshake first, then GetResults
        ble.sendFrame(wbcHandshakeFrame())
        // small delay is sometimes needed; you can do this in a coroutine if you want
        ble.sendFrame(wbcGetResultsFrame())
    }

    fun clearData() {
        ble.sendFrame(wbcClearDataFrame())
    }
}
