package com.example.kotlintest.screens.theremometer

import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.lifecycle.viewModelScope
import com.contec.htd.code.bean.HistoryResultData
import com.contec.htd.code.bean.ResultData
import com.contec.htd.code.bean.SdkConstants
import com.contec.htd.code.callback.BluetoothSearchCallback
import com.contec.htd.code.callback.ConnectCallback
import com.contec.htd.code.callback.OnOperateListener
import com.contec.htd.code.connect.ContecSdk
import com.contec.spo2.code.tools.Utils
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.R
import com.example.kotlintest.util.BluetoothRepository
import com.example.kotlintest.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

val values = listOf("HC-08", "TEMP04", "TEMP05")

data class ThermometerState(
    val temp: Float = 33F,
    val title: String = "Thermometer",
    val titleIcon: Int = R.drawable.ic_bluetooth_on,
    val cancelIcon: Int = R.drawable.ic_cancel,//TODO use  class to gather these information
    val cancelText: String = "Cancel",


    val shouldRequestBluetooth: Boolean = true,

    val discoveredBluetoothDevices: List<String> = mutableListOf(),
    val deviceArrayList: List<BluetoothDevice> = mutableListOf(),//todo make class to manage callback of the device
    val isConnected: Boolean = false,
    val normalRange: String = "Normal 36.1 - 37.2",

    val stringBuffer: StringBuffer = StringBuffer(),
)

sealed class ThermometerAction {
    data object CheckBluetooth : ThermometerAction()
    data object BluetoothRequestFinished : ThermometerAction()
}

sealed class ThermometerEvents {
    data class ShowMsg(val msg: String) : ThermometerEvents()

}

@HiltViewModel
class ThermometerViewModel @Inject constructor(
    private val sdk: ContecSdk,
    private val bluetoothRepository: BluetoothRepository
) :
    BaseViewModel<ThermometerState, ThermometerEvents, ThermometerAction>(
        initialState = ThermometerState()
    ) {
    private val TAG = "ThermometerViewModel"
    override fun handleAction(action: ThermometerAction) {
        when (action) {
            ThermometerAction.CheckBluetooth -> checkBluetooth()
            ThermometerAction.BluetoothRequestFinished -> mutableState.update {
                it.copy(
                    shouldRequestBluetooth = false
                )
            }
        }

    }

    private fun getManufacturerSpecificData(bytes: ByteArray): ByteArray? {
        var msd: ByteArray? = null
        var i = 0
        while (i < bytes.size - 1) {
            val len = bytes[i]
            val type = bytes[i + 1]

            if (0xFF.toByte() == type) {
                // 截取厂商自定义字段
                msd = ByteArray(len - 1)

                for (j in 0..<len - 1) {
                    msd[j] = bytes[i + j + 2]
                }
                return msd
            } else {
                // 跳过其他字段
                i += len.toInt()
            }
            ++i
        }

        return msd
    }

    var mBluetoothSearchCallback: BluetoothSearchCallback = object : BluetoothSearchCallback {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onContecDeviceFound(device: BluetoothDevice, rssi: Int, record: ByteArray?) {
            var manufactorSpecificString = ""

            if (record != null) {
                val manufactorSpecificBytes: ByteArray? = getManufacturerSpecificData(record)


                if (manufactorSpecificBytes != null) {
                    manufactorSpecificString = String(manufactorSpecificBytes)
                }
            }

            Logger.e(TAG, "device.getName()" + device.getName())
            Logger.e(TAG, "String(record!!)" + Utils.bytesToHexString(record))
            Logger.e(TAG, "manufactorSpecificString" + manufactorSpecificString)

            val stringBuffer = StringBuffer()
            stringBuffer.append(device.getName() + "\n")

            if (manufactorSpecificString != null) {
                if (manufactorSpecificString.contains("DT") && manufactorSpecificString.contains("DATA")) {
                    val index = manufactorSpecificString.indexOf("DT")
                    val date = manufactorSpecificString.substring(index + 2, index + 8)
                    stringBuffer.append(
                        (manufactorSpecificString + "\n"
                                + "有数据, " + "当前时间为" + date)
                    )
                } else if (manufactorSpecificString.contains("DT")) {
                    val index = manufactorSpecificString.indexOf("DT")
                    val date = manufactorSpecificString.substring(index + 2, index + 8)
                    stringBuffer.append(
                        (manufactorSpecificString + "\n"
                                + "没有数据, " + "当前时间为" + date)
                    )
                } else if (manufactorSpecificString.contains("DATA")) {
                    stringBuffer.append(
                        (manufactorSpecificString + "\n"
                                + "有数据，没有时间")
                    )
                }
            }

            if (values.any { device.name.startsWith(it) })
            if (device.name.equals("TEMP05"))
//                &&
//                !mutableState.value.deviceArrayList.contains(device)
            {
                mutableState.update { it.copy(deviceArrayList = it.deviceArrayList + device) }
                mutableState.update { it.copy(discoveredBluetoothDevices = it.discoveredBluetoothDevices + stringBuffer.toString()) }
                sdk.connect(device, callback, mOnOperateListener)
//                                    sdk.startRealtime(startRealtimeCallback)
            }
        }

        var callback: ConnectCallback = object : ConnectCallback {
            override fun onConnectStatus(status: Int) {
                Logger.i(TAG, "onConnectStatus: ${status}")
                if (status == com.contec.spo2.code.bean.SdkConstants.CONNECT_CONNECTED) {
                    viewModelScope.launch {
                        mutableState.update { it.copy(isConnected = true) }
                        mutableState.value.stringBuffer.setLength(0)
                        sdk.stopBluetoothSearch()
                        delay(1000)
                    }
                }

                if (status == com.contec.spo2.code.bean.SdkConstants.CONNECT_DISCONNECTED || status == com.contec.spo2.code.bean.SdkConstants.CONNECT_DISCONNECT_EXCEPTION || status == com.contec.spo2.code.bean.SdkConstants.CONNECT_DISCONNECT_SERVICE_UNFOUND || status == com.contec.spo2.code.bean.SdkConstants.CONNECT_DISCONNECT_NOTIFY_FAIL) {
                    mutableState.update { it.copy(isConnected = false) }
                }
            }
        }


        override fun onSearchError(errorCode: Int) {
            if (errorCode == SdkConstants.SCAN_FAIL_BT_UNSUPPORT) {
                Logger.e(TAG, "this no bluetooth")
            } else if (errorCode == SdkConstants.SCAN_FAIL_BT_DISABLE) {
                Logger.e(TAG, "bluetooth not enable")
            }
        }

        override fun onSearchComplete() {
            Logger.i(TAG, "onSearchComplete: ")

        }
    }
    var mOnOperateListener: OnOperateListener = object : OnOperateListener {
        override fun onFail(currentOperate: Int, errorCode: Int) {
            Logger.e(TAG, "onFail: mOnOperateListener")
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
                    Logger.i(TAG, "onHistoryDataSuccess:   " + text + "C" + "   " + date)
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
            text.let { value ->
                if (value != null && value.isNotEmpty()) {
                    mutableState.update {
                        it.copy(temp = value.dropLast(1).toFloat())

                    }
                }
            }
        }
    }


    private fun checkBluetooth() {
        if (mutableState.value.shouldRequestBluetooth) {
            if (bluetoothRepository.isBluetoothEnabled()) {
                sdk.init(false)
                sdk.startBluetoothSearch(mBluetoothSearchCallback, 1000)

            }
        } else {
            sendEvent(ThermometerEvents.ShowMsg("Please enable bluetooth"))
        }
    }

}