package com.example.kotlintest.core.devicesWorker

import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.bluetooth.WbcBleDevice
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class BloodAnalyzerWorker @Inject constructor(
    private val deviceManager: DeviceManager,
    private val wbcDevice: WbcBleDevice
) : Worker {
    init {
        deviceManager.setDeviceModels(DeviceCategory.WhiteBloodCellAnalyzer)
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val TAG = "BloodAnalyzerWorker"
    override fun startWork(result: (JSONObject) -> Unit) {
        scope.launch {
            wbcDevice.connected.collect {
                Logger.i(TAG, "status: $it")
                if (!it) {
                    startDiscovery(result)
                }
            }
        }
    }

    fun startDiscovery(result: (JSONObject) -> Unit) {
        scope.launch {
            deviceManager.bluetoothScanner.startDiscovery(
                deviceManager.getDeviceModels(),
                retryDelayMillis = 2000L
            ) { device ->
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

    }

    override fun stopWork() {
        deviceManager.bluetoothScanner.stopDiscovery()
        wbcDevice.disconnect()
        scope.cancel()
    }

}