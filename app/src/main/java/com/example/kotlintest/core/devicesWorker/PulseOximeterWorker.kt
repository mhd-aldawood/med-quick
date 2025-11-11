package com.example.kotlintest.core.devicesWorker

import android.content.Context
import com.contec.spo2.code.bean.DayStepsData
import com.contec.spo2.code.bean.EcgData
import com.contec.spo2.code.bean.FiveMinStepsData
import com.contec.spo2.code.bean.PieceData
import com.contec.spo2.code.bean.SdkConstants
import com.contec.spo2.code.bean.SpO2PointData
import com.contec.spo2.code.callback.CommunicateCallback
import com.contec.spo2.code.callback.ConnectCallback
import com.contec.spo2.code.callback.RealtimeCallback
import com.contec.spo2.code.callback.RealtimeSpO2Callback
import com.contec.spo2.code.connect.ContecSdk
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.model.ConnectionState
import com.example.kotlintest.util.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import java.util.Timer
import javax.inject.Inject

class PulseOximeterWorker @Inject constructor(
    private val sdk: ContecSdk,
    @ApplicationContext private val context: Context,
    private val timer: Timer,
    private val deviceManager: DeviceManager
) : Worker {
    private val TAG = "PulseOximeterWorker"
    private val realtimeCallback = object : RealtimeCallback {
        override fun onRealtimeWaveData(
            signal: Int, prSound: Int, waveData: Int, barData: Int, fingerOut: Int
        ) {
        }

        override fun onSpo2Data(
            piError: Int, spo2: Int, pr: Int, pi: Int,
            breathRateError: Int, breathRate: Int
        ) {
            var msg = ""
            if (pi > 0) {
                val floatPi = (pi / 10).toFloat();
                msg = "spo2: " + spo2 + "  pr: " + pr + "  piError: " + piError +
                        "  pi: " + String.format(
                    "%.2f",
                    floatPi
                ) + "  breathRateError: " + breathRateError +
                        "  breathRate: " + breathRate
            } else {
                msg = "spo2: " + spo2 + "  pr: " + pr + "  piError: " + piError +
                        "  pi: " + pi + "  breathRateError: " + breathRateError +
                        "  breathRate: " + breathRate
            }
            Logger.i(TAG, "onSpo2Data: ${msg}")
        }

        override fun onRealtimeEnd() {
        }

        override fun onFail(errorCode: Int) {
            Logger.i(TAG, "onFail:startRealtimeCallback ")
        }

    }
    private val communicateCallback = object : CommunicateCallback {
        override fun onPointSpO2DataResult(pointSpO2Datas: ArrayList<SpO2PointData?>?) {
            Logger.i(TAG, "onPointSpO2DataResult:communicateCallback ")
            if (pointSpO2Datas != null) {
                val stringBuffer = StringBuffer()
                for (i in pointSpO2Datas.indices) {
                    val spO2PointData: SpO2PointData? = pointSpO2Datas.get(i)
                    stringBuffer.append("date = " + spO2PointData?.getDate() + "\n")
                    stringBuffer.append("spo2 = " + spO2PointData?.getSpo2Data() + "   ")
                    stringBuffer.append("pr = " + spO2PointData?.getPrData() + "\n")
                }

                Logger.i(TAG, "onPointSpO2DataResult: ${stringBuffer.toString()}")
            }
        }

        override fun onDayStepsDataResult(p0: ArrayList<DayStepsData?>?) {
        }

        override fun onFiveMinStepsDataResult(p0: ArrayList<FiveMinStepsData?>?) {
        }

        override fun onEachPieceDataResult(p0: PieceData?) {
        }

        override fun onEachEcgDataResult(p0: EcgData?) {
        }

        override fun onDataResultEmpty() {
        }

        override fun onDataResultEnd() {
        }

        override fun onDeleteSuccess() {
        }

        override fun onDeleteFail() {
        }

        override fun onFail(p0: Int) {
            Logger.e(TAG, "onFail:communicateCallback${p0} ")
        }
    }
    private val realtimeCallbackSpo2 = object : RealtimeSpO2Callback {
        override fun onRealtimeSpo2Data(
            piError: Int, spo2: Int, pr: Int, pi: Int,
            breathRateError: Int, breathRate: Int
        ) {
            var msg = ""

            if (pi > 0) {
                var floatPi = (pi / 10).toFloat()
                msg = "spo2: " + spo2 + "  pr: " + pr + "  piError: " + piError +
                        "  pi: " + String.format(
                    "%2f",
                    floatPi
                ) + "  breathRateError: " + breathRateError +
                        "  breathRate: " + breathRate
            } else {
                msg = "spo2: " + spo2 + "  pr: " + pr + "  piError: " + piError +
                        "  pi: " + pi + "  breathRateError: " + breathRateError +
                        "  breathRate: " + breathRate
            }
//            mutableState.update { it ->
//                val updatedCardList =
//                    it.pulseOximeterCardList.toMutableList()
//                updatedCardList[0] = updatedCardList[0].copy(value = pr.toString())
//                updatedCardList[1] = updatedCardList[1].copy(value = spo2.toString())
//
//                it.copy(
//                    pulseOximeterCardList = updatedCardList
//                )
//            }
//
//            viewModelScope.launch(Dispatchers.Main) {
//                Logger.i(TAG, "onRealtimeSpo2Data: ${msg}")
//            }


        }

        override fun onRealtimeSpo2End() {
        }

        override fun onFail(p0: Int) {
//            viewModelScope.launch(Dispatchers.Main) {
//                Logger.e(TAG, "onFail:realtimeCallbackSpo2 ${p0}")
//            }
        }
    }
    private val callback = object : ConnectCallback {
        override fun onConnectStatus(status: Int) {
            Logger.i(TAG, "onConnectStatus: ${status}")
            if (status == SdkConstants.CONNECT_CONNECTED) {
                deviceManager.setConnectionState(ConnectionState.Connected)
//                    delay(1000)
//                    sdk.communicate(communicateCallback)
                sdk.startRealtimeSpO2(realtimeCallbackSpo2)

            }

            if (status == SdkConstants.CONNECT_DISCONNECTED || status == SdkConstants.CONNECT_DISCONNECT_EXCEPTION || status == SdkConstants.CONNECT_DISCONNECT_SERVICE_UNFOUND || status == SdkConstants.CONNECT_DISCONNECT_NOTIFY_FAIL) {
                deviceManager.setConnectionState(ConnectionState.Disconnecting)
            }
        }


        override fun onOpenStatus(p0: Int) {
        }

    }

    override fun startWork(result: (JSONObject) -> Unit) {
        sdk.init(false)
        deviceManager.bluetoothScanner.startDiscovery(deviceManager.getDeviceModels())
        { device ->
            sdk.connect(device, object : ConnectCallback {
                override fun onConnectStatus(status: Int) {
                    if (status == SdkConstants.CONNECT_CONNECTED) {
                        deviceManager.setConnectionState(ConnectionState.Connected)
                        sdk.startRealtimeSpO2(object : RealtimeSpO2Callback {
                            override fun onRealtimeSpo2Data(
                                piError: Int, spo2: Int, pr: Int, pi: Int,
                                breathRateError: Int, breathRate: Int
                            ) {
                                val jsonObject = JSONObject()
                                jsonObject.put("pr", pr)
                                jsonObject.put("spo2", spo2)
                            }

                            override fun onRealtimeSpo2End() {
                                TODO("Not yet implemented")
                            }

                            override fun onFail(p0: Int) {
                                TODO("Not yet implemented")
                            }

                        })
                    }

                    if (status == SdkConstants.CONNECT_DISCONNECTED || status == SdkConstants.CONNECT_DISCONNECT_EXCEPTION || status == SdkConstants.CONNECT_DISCONNECT_SERVICE_UNFOUND || status == SdkConstants.CONNECT_DISCONNECT_NOTIFY_FAIL) {
                        deviceManager.setConnectionState(ConnectionState.Disconnecting)
                    }
                }

                override fun onOpenStatus(p0: Int) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    override fun stopWork() {
        sdk.disconnect()
    }

}