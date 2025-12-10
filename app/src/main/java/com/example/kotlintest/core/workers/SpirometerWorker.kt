package com.example.kotlintest.core.workers

import android.content.Context
import com.contec.sp.code.bean.ResultData
import com.contec.sp.code.bean.SdkConstants
import com.contec.sp.code.callback.ConnectCallback
import com.contec.sp.code.callback.OnOperateListener
import com.contec.sp.code.connect.ContecSdk
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.util.Logger
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
import javax.inject.Inject

class SpirometerWorker @Inject constructor(
    @ApplicationContext val context: Context,
    val deviceManager: DeviceManager,
    val sdk: ContecSdk
) : Worker {
    private val _connected = MutableStateFlow(false)
    val connected: StateFlow<Boolean> = _connected.asStateFlow()
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var callback: ((JSONObject) -> Unit)? = null
    private val TAG = "SpirometerWorker"
    init {
        deviceManager.setDeviceModels(DeviceCategory.Spirometer)
        sdk.init(false)
        sdk.setOnOperateListener(object : OnOperateListener {
            override fun onSuccess(p0: Int, p1: ResultData?) {
                _connected.value = true
                p1.let {
                    Logger.i(TAG,it.toString())
                    callback?.invoke(JSONObject(it.toString()))
                }

            }

            override fun onFail(p0: Int, p1: Int) {
                _connected.value = false
            }

            override fun onDataInformation(p0: Int) {
            }

        })
    }

    override fun startWork(result: (JSONObject) -> Unit) {
        scope.launch {
            connected.collect { it ->
                if (!it) {
                    deviceManager.bluetoothScanner.startDiscovery(
                        deviceManager.getDeviceModels(),
                        retryDelayMillis = 1000L
                    ) { device ->
                        callback=result
                        sdk.connect(device, object : ConnectCallback {
                            override fun onConnectStatus(status: Int) {
                                when (status) {
                                    SdkConstants.CONNECT_CONNECTED ->                     //连接成功
                                        _connected.value = true

                                    SdkConstants.CONNECT_DISCONNECTED, SdkConstants.CONNECT_DISCONNECT_EXCEPTION, SdkConstants.CONNECT_DISCONNECT_SERVICE_UNFOUND, SdkConstants.CONNECT_DISCONNECT_NOTIFY_FAIL ->                     //连接断开
                                        _connected.value = false

                                    else -> {}
                                }
                            }

                            override fun onOpenStatus(p0: Int) {
                            }

                        })
                    }
                }
            }
        }
    }

    override fun stopWork() {
        deviceManager.bluetoothScanner.stopDiscovery()
        sdk.disconnect()
        scope.cancel()
    }
}