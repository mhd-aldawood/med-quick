package com.example.kotlintest.core.workers

import android.content.Context
import com.contec.bc.code.bean.DataBean
import com.contec.bc.code.bean.IndexDataBean
import com.contec.bc.code.callback.ConnectCallback
import com.contec.bc.code.callback.OnOperateListener
import com.contec.bc.code.connect.ContecSdk
import com.contec.spo2.code.bean.SdkConstants
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.screens.home.models.DeviceCategory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.ArrayList
import javax.inject.Inject

class UrineAnalyzerWorker @Inject constructor(
    val deviceManager: DeviceManager, val sdk: ContecSdk
) : Worker {

    init {
        deviceManager.setDeviceModels(DeviceCategory.Thermometer)
        sdk.init(false)
    }

    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val _connectStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val connectStatus: StateFlow<Boolean> = _connectStatus.asStateFlow()

    override fun startWork(result: (JSONObject) -> Unit) {
        scope.launch {
            connectStatus.collect {
                if (!it)
                    deviceManager.bluetoothScanner.startDiscovery(
                        deviceManager.getDeviceModels(),
                        retryDelayMillis = 1000L
                    ) { device ->
                        sdk.connect(device,
                            object : ConnectCallback {
                            override fun onConnectStatus(status: Int) {
                                if (status == SdkConstants.CONNECT_CONNECTED){
                                    _connectStatus.value=true
                                }
                                else if(status == SdkConstants.CONNECT_DISCONNECTED || status == SdkConstants.CONNECT_DISCONNECT_EXCEPTION || status == SdkConstants.CONNECT_DISCONNECT_SERVICE_UNFOUND || status == SdkConstants.CONNECT_DISCONNECT_NOTIFY_FAIL){
                                    _connectStatus.value=false
                                }

                            }

                        },
                            object : OnOperateListener
                            {
                            override fun onFail(p0: Int, p1: Int) {
                            }

                            override fun onFirmwareVersion(p0: String?) {
                            }

                            override fun setTimeSuccess() {
                            }

                            override fun onStorageInfo(p0: Int) {
                            }

                            override fun onDeleteSuccess() {
                            }

                            override fun onDataEmpty() {
                            }

                            override fun onCommunicateSuccess(p0: ArrayList<DataBean?>?) {
                                p0.let {
                                    it?.forEach {dataBean->
                                        val jsonObject = JSONObject().apply {
                                            put("date", dataBean?.date)

                                            put("URO", dataBean?.URO)
                                            put("BLD", dataBean?.BLD)
                                            put("BIL", dataBean?.BIL)
                                            put("KET", dataBean?.KET)
                                            put("GLU", dataBean?.GLU)

                                            put("PRO", dataBean?.PRO)
                                            put("PH", dataBean?.PH)
                                            put("NIT", dataBean?.NIT)
                                            put("LEU", dataBean?.LEU)
                                            put("SG", dataBean?.SG)

                                            put("VC", dataBean?.VC)
                                            put("MAL", dataBean?.MAL)
                                            put("CR", dataBean?.CR)
                                            put("UCA", dataBean?.UCA)
                                        }
                                        result(jsonObject)
                                    }
                                }
                            }

                            override fun onCommunicateIndexSuccess(p0: ArrayList<IndexDataBean?>?) {
                            }

                            override fun onCommunicateDisplaySuccess(p0: String?) {
                            }

                            override fun onCommunicateIndexSuccess(p0: String?) {
                            }

                            override fun onCommunicateSuccess(p0: String?) {
                            }

                        })

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

