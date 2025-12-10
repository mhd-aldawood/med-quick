package com.example.kotlintest.core.workers

import com.contec.htd.code.bean.HistoryResultData
import com.contec.htd.code.bean.ResultData
import com.contec.htd.code.callback.ConnectCallback
import com.contec.htd.code.callback.OnOperateListener
import com.contec.htd.code.connect.ContecSdk
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.model.ConnectionState
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


class ThermometerWorker @Inject constructor(
    private val sdk: ContecSdk,
    private val deviceManager: DeviceManager
) : Worker {
    init {
        deviceManager.setDeviceModels(DeviceCategory.Thermometer)
        sdk.init(false)
    }
    private val TAG = "ThermometerWorker"
    private val _connected = MutableStateFlow(false)
    val connected: StateFlow<Boolean> = _connected.asStateFlow()
    var callback: ConnectCallback = object : ConnectCallback {
        override fun onConnectStatus(status: Int) {
            Logger.i(TAG, "onConnectStatus: ${status}")
            if (status == com.contec.spo2.code.bean.SdkConstants.CONNECT_CONNECTED) {
                deviceManager.setConnectionState(ConnectionState.Connected)
                _connected.value = true
            }


            if (status == com.contec.spo2.code.bean.SdkConstants.CONNECT_DISCONNECTED || status == com.contec.spo2.code.bean.SdkConstants.CONNECT_DISCONNECT_EXCEPTION || status == com.contec.spo2.code.bean.SdkConstants.CONNECT_DISCONNECT_SERVICE_UNFOUND || status == com.contec.spo2.code.bean.SdkConstants.CONNECT_DISCONNECT_NOTIFY_FAIL) {
                deviceManager.setConnectionState(ConnectionState.Disconnecting)
                _connected.value = false
            }
        }
    }
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override fun startWork(result: (JSONObject) -> Unit) {
        scope.launch {
            connected.collect { it ->
                if (!it) {
                    deviceManager.bluetoothScanner.startDiscovery(
                        deviceManager.getDeviceModels(),
                        retryDelayMillis = 1000L
                    ) { device ->
                        sdk.connect(device, callback, object : OnOperateListener {
                            override fun onFail(currentOperate: Int, errorCode: Int) {
                                Logger.e(TAG, "onFail: mOnOperateListener")
                                _connected.value = false
                            }

                            /**
                             * 对时成功
                             */
                            override fun onSetTimeSuccess() {
                                Logger.e(TAG, "onSetTimeSuccess: mOnOperateListener")
                            }

                            /**
                             * 获取空间信息
                             * @param totalNumber
                             */
                            override fun onStorageInfoSuccess(totalNumber: Int) {
                                Logger.i(TAG, "onStorageInfoSuccess: mOnOperateListener")
                            }

                            /**
                             * 历史数据为空
                             */
                            override fun onHistoryDataEmpty() {
                                Logger.i(TAG, "onHistoryDataEmpty: onHistoryDataEmpty")
                            }


                            /**
                             * 获取历史数据
                             * @param historyResultDataArrayList
                             */
                            override fun onHistoryDataSuccess(historyResultDataArrayList: ArrayList<HistoryResultData?>?) {


                                if (historyResultDataArrayList != null) {
                                    for (i in historyResultDataArrayList.indices) {
                                        val year = historyResultDataArrayList.get(i)!!.getYear()
                                        val month = historyResultDataArrayList.get(i)!!.getMonth()
                                        val day = historyResultDataArrayList.get(i)!!.getDay()
                                        val hour = historyResultDataArrayList.get(i)!!.getHour()
                                        val min = historyResultDataArrayList.get(i)!!.getMin()
                                        val sec = historyResultDataArrayList.get(i)!!.getSec()

                                        val date =
                                            year.toString() + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec
                                        val text = historyResultDataArrayList.get(i)!!.getTemp()
                                        Logger.i(
                                            TAG,
                                            "onHistoryDataSuccess:   " + text + "C" + "   " + date
                                        )
                                    }
                                }
                            }


                            /**
                             * 获取实时测量数据
                             * @param resultData
                             */
                            override fun onRealtimeDataSuccess(resultData: ResultData) {
                                var text: String? = null
                                if (resultData.temp != null) {
                                    text = resultData.temp
                                } else if (resultData.error != null) {
                                    text = resultData.error
                                }
                                Logger.i(TAG, "onRealtimeDataSuccess: ${text}")
                                _connected.value = true
                                text.let { value ->
                                    if (value != null && value.isNotEmpty()) {
                                        val JSONObject = JSONObject()
                                        JSONObject.put("temp", value.dropLast(1))
                                        result(JSONObject)
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
    }

    override fun stopWork() {
        sdk.disconnect()
        deviceManager.bluetoothScanner.stopDiscovery()
        scope.cancel()
    }

}