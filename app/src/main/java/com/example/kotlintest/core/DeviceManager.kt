package com.example.kotlintest.core

import com.example.kotlintest.core.bluetooth.BluetoothScanner
import com.example.kotlintest.core.model.ConnectionState
import com.example.kotlintest.screens.home.models.DeviceCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DeviceConfigure(
    val connectionState: ConnectionState = ConnectionState.Idle,
    var deviceCategory: DeviceCategory? = null
)

class DeviceManager @Inject constructor(val bluetoothScanner: BluetoothScanner) {
    private val _deviceConfigure = MutableStateFlow(DeviceConfigure())
    val deviceConfigure: StateFlow<DeviceConfigure> = _deviceConfigure

    fun setConnectionState(state: ConnectionState) {
        _deviceConfigure.update { it.copy(connectionState = state) }
    }
    private var deviceModels: List<String>? = null

    fun getDeviceModels(): List<String> = deviceModels!!


    fun setDeviceType(deviceCategory: DeviceCategory) {
        setDeviceModels(deviceCategory)
    }

    fun setDeviceModels(deviceCategory: DeviceCategory) {//TODO we can read from storage
        deviceModels = when (deviceCategory) {
            DeviceCategory.DigitalStethoscope -> listOf("Mintti")
            DeviceCategory.DopplerUltrasound -> TODO()
            DeviceCategory.ECGWorkstation -> listOf<String>(
                "ECGWS",
                "8000GW",
                "CB100",
            )
            DeviceCategory.ElectronicSphygmomanometer -> listOf(
                "NIBP01",
                "NIBP03",
                "NIBP04",
                "NIBP07",
                "NIBP08",
                "NIBP09",
                "NIBP11"
            )
            DeviceCategory.FIATestingSystemPOCT -> TODO()
            DeviceCategory.Glucometer -> TODO()
            DeviceCategory.HemoglobinTestingSystem -> TODO()
            DeviceCategory.LipidTestingSystem -> TODO()
            DeviceCategory.PulseOximeter -> listOf(
                "SpO208",
                "SpO201",
                "SpO202",
                "SpO206",
                "SpO209",
                "SpO210",
                "SpO212",
                "SpO213"
            )

            DeviceCategory.Thermometer -> listOf("HC-08", "TEMP04", "TEMP05")

            DeviceCategory.Spirometer -> TODO()

            DeviceCategory.UrineAnalyzer -> TODO()
            DeviceCategory.WhiteBloodCellAnalyzer -> TODO()
        }


    }

    /*
    *
    *
    * THE PURPOSE FROM THIS METHOD IS TO CHECK IF I AM STILL CONNECTED TO THE DEVICE AND THE STATUS OR NOT
    * */
    public fun isConnected(): Boolean =
        deviceConfigure.value.connectionState == ConnectionState.Connected
}

interface DeviceOperation {
    fun ConnectToDevice()
}