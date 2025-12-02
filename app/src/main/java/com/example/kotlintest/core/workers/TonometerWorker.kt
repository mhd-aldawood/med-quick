package com.example.kotlintest.core.workers

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.contec.bp.code.base.ContecDevice
import com.contec.bp.code.bean.ContecBluetoothType
import com.contec.bp.code.callback.BluetoothSearchCallback
import com.contec.bp.code.callback.CommunicateCallback
import com.contec.bp.code.connect.ContecSdk
import com.contec.bp.code.tools.Utils
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.screens.tonometer.models.TonometerModel
import com.example.kotlintest.util.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TonometerWorker @Inject constructor(
    private val sdk: ContecSdk,
    @ApplicationContext private val context: Context,
    private val deviceManager: DeviceManager
) : Worker {
    init {
        sdk.init(ContecBluetoothType.TYPE_FF, false)
    }
    private val TAG = "TonometerWorker"


    override fun startWork(result: (JSONObject) -> Unit) {
        sdk.startBluetoothSearch(
            object : BluetoothSearchCallback {
                override fun onContecDeviceFound(contecDevice: ContecDevice?) {

                    if (contecDevice?.name == null) {
                        return
                    }

                    //打印设备名称
                    Logger.e(TAG, contecDevice.getName())


                    if (deviceManager.getDeviceModels().any { contecDevice.name.startsWith(it) }) {
                        Logger.i(TAG, Utils.bytesToHexString(contecDevice.getRecord()))
                        sdk.startCommunicate(
                            context, contecDevice, object : CommunicateCallback {
                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onCommunicateSuccess(json: String?) {
                                    Logger.i(TAG, "onCommunicateSuccess" + json)

                                    try {
                                        var jsonArray =
                                            JSONObject(json).getJSONArray("BloodPressureData")

                                        for (i in 0..<jsonArray.length()) {
                                            val jsonObject: JSONObject = jsonArray.optJSONObject(i)
                                            val systolicPressure =
                                                jsonObject.optString("SystolicPressure")
                                            val diastolicPressure =
                                                jsonObject.optString("DiastolicPressure")
                                            val date = jsonObject.optString("Date")
                                        }
                                    } catch (e: JSONException) {
                                        Logger.e(TAG, e.message.toString())
                                    }
                                    json?.let {
                                        val model: TonometerModel = jsonDecoder.decodeFromString(it)
                                        val latestScan = model.BloodPressureData.maxByOrNull {
                                            LocalDateTime.parse(
                                                it.Date,
                                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                            )
                                        }
                                        Logger.i(
                                            TAG,
                                            "onCommunicateSuccess: " + latestScan?.SystolicPressure
                                        )
                                        Logger.i(
                                            TAG,
                                            "onCommunicateSuccess: " + latestScan?.PulseRate
                                        )
                                        if (latestScan?.SystolicPressure != null && latestScan?.PulseRate != null) {
                                            val jsonObject = JSONObject()
                                            jsonObject.put(
                                                "pressureValue",
                                                "${latestScan?.SystolicPressure} / ${latestScan?.PulseRate}"
                                            )
                                            jsonObject.put(
                                                "systolicPressure",
                                                latestScan?.SystolicPressure?.toDouble() ?: 0.0
                                            )
                                            jsonObject.put(
                                                "pulseRate",
                                                latestScan?.PulseRate?.toDouble() ?: 0.0
                                            )
                                            result(jsonObject)
                                        }

                                    }

                                }

                                override fun onCommunicateFailed(p0: Int) {
                                    Logger.e(TAG, "onCommunicateFailed" + p0.toString())

                                }

                                override fun onCommunicateProgress(p0: Int) {
                                    Logger.e(TAG, "onCommunicateProgress" + p0.toString())
                                }

                            }
                        )
                    }
                }

                override fun onSearchError(p0: Int) {
                    Logger.e(TAG, "onSearchError")
                }

                override fun onSearchComplete() {
                    Logger.e(TAG, "onSearchComplete")

                }
            }, 200000
        )
    }

    override fun stopWork() {
        sdk.stopBluetoothSearch()
        sdk.stopCommunicate()
        deviceManager.bluetoothScanner.stopDiscovery()
    }

    private val jsonDecoder = Json { ignoreUnknownKeys = true } // Ignore unknown fields

}


