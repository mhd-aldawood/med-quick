package com.example.kotlintest.core

import com.example.kotlintest.core.model.ConnectionState
import com.example.kotlintest.screens.home.DeviceCategory
import com.example.kotlintest.util.BluetoothRepositoryImpl
import javax.inject.Inject

data class DeviceConfigure(
    val connectionState: ConnectionState,
    var deviceCategory: DeviceCategory? = null
)


class DeviceManager @Inject constructor(val bluetoothRepositoryImpl: BluetoothRepositoryImpl) {
    val deviceConfigure: DeviceConfigure =
        DeviceConfigure(
            connectionState = ConnectionState.Idle,
        )
    private var deviceModels: List<String>? = null

    fun getDeviceModels(): List<String> = deviceModels!!



    fun setDeviceType(deviceCategory: DeviceCategory) {
        deviceConfigure.deviceCategory = deviceCategory
        setDeviceModels(deviceCategory)
    }

    fun setDeviceModels(deviceCategory: DeviceCategory) {//TODO we can read from storage
        deviceModels = when (deviceCategory) {
            DeviceCategory.DigitalStethoscope -> TODO()
            DeviceCategory.DopplerUltrasound -> TODO()
            DeviceCategory.ECGWorkstation -> TODO()
            DeviceCategory.ElectronicSphygmomanometer -> TODO()
            DeviceCategory.FIATestingSystemPOCT -> TODO()
            DeviceCategory.Glucometer -> TODO()
            DeviceCategory.HemoglobinTestingSystem -> TODO()
            DeviceCategory.LipidTestingSystem -> TODO()
            DeviceCategory.PulseOximeter -> listOf("SpO208", "SpO201", "SpO202", "SpO206", "SpO209", "SpO210", "SpO212", "SpO213")
            DeviceCategory.Thermometer -> listOf("HC-08", "TEMP04", "TEMP05")

            DeviceCategory.Spirometer -> TODO()

            DeviceCategory.UrineAnalyzer -> TODO()
            DeviceCategory.WhiteBloodCellAnalyzer -> TODO()
        }


    }
}

interface DeviceOperation {
    fun ConnectToDevice()
}