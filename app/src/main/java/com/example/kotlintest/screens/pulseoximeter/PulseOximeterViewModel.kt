package com.example.kotlintest.screens.pulseoximeter

import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.lifecycle.viewModelScope
import com.contec.spo2.code.bean.DayStepsData
import com.contec.spo2.code.bean.EcgData
import com.contec.spo2.code.bean.FiveMinStepsData
import com.contec.spo2.code.bean.PieceData
import com.contec.spo2.code.bean.SdkConstants
import com.contec.spo2.code.bean.SpO2PointData
import com.contec.spo2.code.callback.BluetoothSearchCallback
import com.contec.spo2.code.callback.CommunicateCallback
import com.contec.spo2.code.callback.ConnectCallback
import com.contec.spo2.code.callback.RealtimeCallback
import com.contec.spo2.code.callback.RealtimeSpO2Callback
import com.contec.spo2.code.connect.ContecSdk
import com.contec.spo2.code.tools.Utils
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.DeviceOperation
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.core.workers.Worker
import com.example.kotlintest.core.model.ConnectionState
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.di.PulseOximeterQualifier
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.screens.pulseoximeter.models.PulseOximeterCard
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class PulseOximeterState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "Pulse Oximeter",
        titleIcon = R.drawable.ic_bluetooth_on,
        cancelIcon = R.drawable.ic_cancel
    ),
    val pulseOximeterCardList: List<PulseOximeterCard> = listOf<PulseOximeterCard>(
        PulseOximeterCard(
            value = "90",
            cardColor = PrimaryMidLinkColor,
            title = "Pulse",
            cardUnit = "bpm",
            normalRange = "65 - 80"
        ),
        PulseOximeterCard(
            value = "94",
            cardColor = FrenchWine,
            title = "SpO2",
            cardUnit = "%",
            normalRange = "95 - 100"
        )
    )
)

sealed class PulseOximeterAction {
    data class Bluetooth(val command: BluetoothCommand) : PulseOximeterAction()

}

sealed class PulseOximeterEvents {
    data class ShowMsg(val msg: String) : PulseOximeterEvents()
}

@HiltViewModel
class PulseOximeterViewModel @Inject constructor(
    private val sdk: ContecSdk,
    private val deviceManager: DeviceManager,
    @PulseOximeterQualifier private val worker: Worker
) :
    BaseViewModel<PulseOximeterState, PulseOximeterEvents, PulseOximeterAction>(
        initialState = PulseOximeterState()
    ), DeviceOperation {
    private val TAG = "PulseOximeterViewModel"



    init {
        deviceManager.setDeviceModels(DeviceCategory.PulseOximeter)
    }



    override fun handleAction(action: PulseOximeterAction) {
        when (action) {
            is PulseOximeterAction.Bluetooth -> when (action.command) {
                BluetoothCommand.SearchAndCommunicate -> ConnectToDevice()
                BluetoothCommand.StopBluetoothAndCommunication -> worker.stopWork()
            }
        }
    }

    private val callback = object : ConnectCallback {
        override fun onConnectStatus(status: Int) {
            Logger.i(TAG, "onConnectStatus: ${status}")
            if (status == SdkConstants.CONNECT_CONNECTED) {
                viewModelScope.launch(Dispatchers.IO) {
                    deviceManager.setConnectionState(ConnectionState.Connected)
                    sdk.stopBluetoothSearch()
                    delay(1000)
//                    sdk.communicate(communicateCallback)
                    sdk.startRealtimeSpO2(realtimeCallbackSpo2)
//                    sdk.startRealtime(realtimeCallback)
                }
            }

            if (status == SdkConstants.CONNECT_DISCONNECTED || status == SdkConstants.CONNECT_DISCONNECT_EXCEPTION || status == SdkConstants.CONNECT_DISCONNECT_SERVICE_UNFOUND || status == SdkConstants.CONNECT_DISCONNECT_NOTIFY_FAIL) {
                deviceManager.setConnectionState(ConnectionState.Disconnecting)
            }
        }


        override fun onOpenStatus(p0: Int) {
        }

    }
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
            mutableState.update { it ->
                val updatedCardList =
                    it.pulseOximeterCardList.toMutableList()
                updatedCardList[0] = updatedCardList[0].copy(value = pr.toString())
                updatedCardList[1] = updatedCardList[1].copy(value = spo2.toString())

                it.copy(
                    pulseOximeterCardList = updatedCardList
                )
            }

            viewModelScope.launch(Dispatchers.Main) {
                Logger.i(TAG, "onRealtimeSpo2Data: ${msg}")
            }


        }

        override fun onRealtimeSpo2End() {
        }

        override fun onFail(p0: Int) {
            viewModelScope.launch(Dispatchers.Main) {
                Logger.e(TAG, "onFail:realtimeCallbackSpo2 ${p0}")
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

    override fun ConnectToDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            if (deviceManager.bluetoothScanner.isBluetoothEnabled()) {
                sdk.init(false)
                sdk.startBluetoothSearch(
                    object : BluetoothSearchCallback {
                        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
                        override fun onDeviceFound(
                            device: BluetoothDevice,
                            rssi: Int,
                            record: ByteArray
                        ) {
                            val address = device.getAddress()
                            viewModelScope.launch(Dispatchers.Default) {
                                Logger.i(TAG, "device name" + device.getName())


                                Logger.i(TAG, "BYTE = " + Utils.bytesToHexString(record))

                                var manufactorSpecificString = ""

                                if (record != null) {
                                    val manufactorSpecificBytes =
                                        getManufacturerSpecificData(record)

                                    if (manufactorSpecificBytes != null) {
                                        manufactorSpecificString = String(manufactorSpecificBytes)
                                    }
                                }

                                Logger.e(TAG, "String(record) ${String(record)} ")
                                Logger.e(
                                    TAG,
                                    "manufactorSpecificString ${manufactorSpecificString}"
                                )

                                val stringBuffer = StringBuffer()
                                stringBuffer.append(device.getName() + "\n")

                                if (manufactorSpecificString != null) {
                                    if (manufactorSpecificString.contains("DT") && manufactorSpecificString.contains(
                                            "DATA"
                                        )
                                    ) {
                                        val index = manufactorSpecificString.indexOf("DT")
                                        val date =
                                            manufactorSpecificString.substring(index + 2, index + 8)
                                        stringBuffer.append(
                                            manufactorSpecificString +
                                                    "There is data, " + "The current time is" + date
                                        )
                                    } else if (manufactorSpecificString.contains("DT")) {
                                        val index = manufactorSpecificString.indexOf("DT")
                                        val date =
                                            manufactorSpecificString.substring(index + 2, index + 8)
                                        stringBuffer.append(
                                            (manufactorSpecificString + "\n"
                                                    + "no data, " + "The current time is" + date)
                                        )
                                    } else if (manufactorSpecificString.contains("DATA")) {
                                        stringBuffer.append(
                                            manufactorSpecificString
                                                    + "There is data, but no time"
                                        )
                                    }

                                }
                                Logger.i(TAG, "device: ${device.name}")
                                Logger.i(TAG, "stringBuffer: ${stringBuffer}")
                                if (deviceManager.getDeviceModels()
                                        .any { device.name.startsWith(it) }
                                ) {
                                    withContext(Dispatchers.IO) {
                                        sdk.connect(device, callback)
                                    }
//                                    sdk.startRealtime(startRealtimeCallback)
                                }
                            }

                        }

                        override fun onSearchError(errorCode: Int) {
                            deviceManager.setConnectionState(ConnectionState.Failed)
                        }

                        override fun onSearchComplete() {

                        }
                    }, 200000
                )

//                worker.startWork {jSONObject ->
//                    mutableState.update { it ->
//                        val updatedCardList =
//                            it.pulseOximeterCardList.toMutableList()
//                        updatedCardList[0] = updatedCardList[0].copy(value = jSONObject.getInt("pr").toString())
//                        updatedCardList[1] = updatedCardList[1].copy(value = jSONObject.getInt("spo2").toString())
//
//                        it.copy(
//                            pulseOximeterCardList = updatedCardList
//                        )
//                    }
//                }



            } else {
                //you should enable bluetooth msg
                sendEvent(PulseOximeterEvents.ShowMsg("Please enable bluetooth"))
            }

        }
    }

}