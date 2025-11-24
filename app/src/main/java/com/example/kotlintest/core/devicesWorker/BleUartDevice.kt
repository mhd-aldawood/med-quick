package com.example.kotlintest.core.devicesWorker

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import com.example.kotlintest.screens.bloodanalyzer.models.WbcBleUuids
import com.example.kotlintest.util.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class BleUartDevice @Inject constructor(
    private val context: Context,
) {
    lateinit var device: BluetoothDevice
    private var gatt: BluetoothGatt? = null
    private var txChar: BluetoothGattCharacteristic? = null   // write to device
    private var rxChar: BluetoothGattCharacteristic? = null   // notifications from device
    private val rxBuffer = mutableListOf<Byte>()   // 🔹 new


    private val frameChannel = Channel<ByteArray>(Channel.BUFFERED)
    val framesFlow = frameChannel.receiveAsFlow()

    fun init(device: BluetoothDevice) {
        this.device = device
    }

    fun connect() {
        gatt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            device.connectGatt(context, false, callback, BluetoothDevice.TRANSPORT_LE)
        } else {
            device.connectGatt(context, false, callback)
        }
    }

    fun disconnect() {
        gatt?.close()
        gatt = null
    }

    fun sendFrame(frame: ByteArray): Boolean {
        val g = gatt ?: return false
        val tx = txChar ?: return false
        tx.value = frame
        tx.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        return g.writeCharacteristic(tx)
    }

    private val TAG = "BleUartDevice"

    private val callback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(g: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                g.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                disconnect()
            }
        }

        override fun onServicesDiscovered(g: BluetoothGatt, status: Int) {
            if (status != BluetoothGatt.GATT_SUCCESS) return

            for (service in g.services) {
                Logger.d(TAG, "Service: ${service.uuid}")
                for (ch in service.characteristics) {
                    Logger.d(TAG, "  Char: ${ch.uuid} props=${ch.properties}")
                }
            }

            // Service used for writing commands
            val writeService = g.getService(WbcBleUuids.SERVICE_WRITE_UUID)
            txChar = writeService?.getCharacteristic(WbcBleUuids.CHAR_WRITE_UUID)

            // Service used for receiving notifications
            val notifyService = g.getService(WbcBleUuids.SERVICE_NOTIFY_UUID)
            rxChar = notifyService?.getCharacteristic(WbcBleUuids.CHAR_NOTIFY_UUID)

            rxChar?.let { rx ->
                g.setCharacteristicNotification(rx, true)
                val cccd = rx.getDescriptor(WbcBleUuids.CCCD_UUID)
                cccd?.let { descriptor ->
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    g.writeDescriptor(descriptor)
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
//            if (characteristic.uuid == WbcBleUuids.CHAR_NOTIFY_UUID) {
//                val data = characteristic.value ?: return
//                frameChannel.trySend(data.copyOf())
//            }
            if (characteristic.uuid != WbcBleUuids.CHAR_NOTIFY_UUID) return

            val chunk = characteristic.value ?: return
            Logger.d("BLE", "RX chunk: " + chunk.joinToString(" ") { "%02X".format(it) })

            synchronized(rxBuffer) {
                rxBuffer.addAll(chunk.toList())
                processRxBuffer()
            }
        }
    }

    private fun processRxBuffer() {
        while (true) {
            // Need at least header + len
            if (rxBuffer.size < 3) return

            // 1) Find header A5 55
            var startIndex = -1
            for (i in 0 until rxBuffer.size - 1) {
                if (rxBuffer[i] == 0xA5.toByte() && rxBuffer[i + 1] == 0x55.toByte()) {
                    startIndex = i
                    break
                }
            }

            if (startIndex == -1) {
                // No header found → drop garbage
                rxBuffer.clear()
                return
            }

            // Drop bytes before header if any
            if (startIndex > 0) {
                repeat(startIndex) { rxBuffer.removeAt(0) }
            }

            if (rxBuffer.size < 3) return

            val len = rxBuffer[2].toInt() and 0xFF
            val totalSize = len + 3  // A5 55 + len + rest

            // Wait until full frame is in buffer
            if (rxBuffer.size < totalSize) return

            // Extract complete frame
            val frameBytes = ByteArray(totalSize)
            for (i in 0 until totalSize) {
                frameBytes[i] = rxBuffer[i]
            }

            // Remove frame from buffer
            repeat(totalSize) { rxBuffer.removeAt(0) }

            Logger.d("WBC", "RX frame: " + frameBytes.joinToString(" ") { "%02X".format(it) })

            frameChannel.trySend(frameBytes)
            // loop again in case another frame is already in buffer
        }
    }

}