package com.example.kotlintest.core.devicesWorker

import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.bluetooth.WbcBleDevice
import com.example.kotlintest.screens.home.models.DeviceCategory
import org.json.JSONObject
import javax.inject.Inject

class BloodAnalyzerWorker @Inject constructor(
    private val deviceManager: DeviceManager,
    private val wbcDevice: WbcBleDevice
) : Worker {
    init {
        deviceManager.setDeviceModels(DeviceCategory.WhiteBloodCellAnalyzer)
    }

    override fun startWork(result: (JSONObject) -> Unit) {
        deviceManager.bluetoothScanner.startDiscovery(deviceManager.getDeviceModels()) { device ->
            wbcDevice.init(device)
            wbcDevice.connect() { cellResult ->
                val jsonObject = JSONObject().apply {
                    put("sn", cellResult.sn)
                    put("deviceSn", cellResult.deviceSn)
                    put("dateTime", cellResult.dateTime)

                    put("wbc", cellResult.wbc)
                    put("lym", cellResult.lym)
                    put("mon", cellResult.mon)
                    put("neu", cellResult.neu)
                    put("eqs", cellResult.eqs)
                    put("bas", cellResult.bas)

                    put("lymPer", cellResult.lymPer)
                    put("monPer", cellResult.monPer)
                    put("neuPer", cellResult.neuPer)
                    put("eosPer", cellResult.eosPer)
                    put("basPer", cellResult.basPer)
                }
                result(jsonObject)

            }
            wbcDevice.requestHandshake()
            wbcDevice.requestLatestResult()
        }

    }

    override fun stopWork() {
        wbcDevice.disconnect()
    }

}