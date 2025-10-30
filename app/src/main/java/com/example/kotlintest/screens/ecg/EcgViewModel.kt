package com.example.kotlintest.screens.ecg

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.viewModelScope
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.model.ConnectionState
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.di.DataUtilsFactory
import com.example.kotlintest.screens.ecg.model.ECGConfiguration
import com.example.kotlintest.screens.ecg.model.NativeMsgBridge
import com.example.kotlintest.util.BluetoothRepository
import com.example.kotlintest.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import serial.jni.BluConnectionStateListener
import serial.jni.DataUtils
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

val devices = listOf<String>(
    "ECGWS",
    "8000GW",
    "CB100",
)
val items = emptyList<String>(

)

data class EcgState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "ECG Device", titleIcon = R.drawable.ic_bluetooth_on
    ),
    val shouldRequestBluetooth: Boolean = true,
    val checkIcon: Int = R.mipmap.ic_check,
    val eCGConfiguration: ECGConfiguration = ECGConfiguration(),
)

sealed class EcgEvents {
    data class ShowToast(val msg: String) : EcgEvents()

    // Wave chunks. Keep these light; the raw arrays can be big.


    // Diagnostics, params, steps
    object SetRenderColor : EcgEvents()
    object MsgInterrupted : EcgEvents()
    data class StartRender(val mEcgQueue: ConcurrentLinkedQueue<Short>) : EcgEvents()

}

sealed class EcgAction {
    data class OnDurationChange(val duration: Int) : EcgAction()
    data object CheckBluetooth : EcgAction()
    data object BluetoothRequestFinished : EcgAction()
    data object StopBluetoothAndGathering : EcgAction()

    data class HeartRate(val bpm: Int) : EcgAction()
    data class LeadOff(val flagOff: String) : EcgAction()
    data class RemainingRecordTime(val toInt: Int) : EcgAction()
    data class NibpResult(
        val toInt: Int,
        val toInt2: Int,
        val toInt3: Int,
        val toInt4: Int,
        val err: Byte
    ) : EcgAction()
}


@HiltViewModel
class EcgViewModel @Inject constructor(
    private val bluetoothRepository: BluetoothRepository,
    private val factory: DataUtilsFactory,
    private val mEcgQueue: ConcurrentLinkedQueue<Short>,
) : BaseViewModel<EcgState, EcgEvents, EcgAction>(initialState = EcgState()) {
    override fun handleAction(action: EcgAction) {
        when (action) {
            EcgAction.CheckBluetooth -> checkBluetooth()
            EcgAction.BluetoothRequestFinished -> mutableState.update {
                it.copy(
                    shouldRequestBluetooth = false
                )
            }

            EcgAction.StopBluetoothAndGathering -> stopBluetoothAndGathering()
            is EcgAction.OnDurationChange -> {
                handleDurationChange()
            }

            is EcgAction.HeartRate -> {
                Logger.i(TAG, action.bpm.toString())
            }

            is EcgAction.LeadOff -> {
                Logger.i(TAG, action.flagOff)
            }

            is EcgAction.NibpResult -> data?.BluNIBPConfirmCmd()
            is EcgAction.RemainingRecordTime -> TODO()
        }
    }

    private fun handleDurationChange(): Unit {}

    val nativeMsg = NativeMsgBridge(//TODO think again for this class
        ecgQueue = mEcgQueue,
        onWaveStable = { stable -> sendEvent(EcgEvents.SetRenderColor) })
    private var data: DataUtils? = null

    suspend fun init(address: String) {

        data = factory.create(
            address = address,
            lead = DataUtils.ECG_LEAD_WILSON,
            debug = false,
            listener = object : BluConnectionStateListener {
                override fun OnBluConnectStart() {
                    viewModelScope.launch {
                        mutableState.update {
                            val state = it.eCGConfiguration.copy(
                                state = ConnectionState.Connecting, connectEnabled = false
                            )
                            it.copy(eCGConfiguration = state)
                        }
                        //sendEvent(EcgEvents.ShowToast("Connect Start"))
                    }
                }

                override fun OnBluConnectSuccess() {
                    viewModelScope.launch {
                        mutableState.update {
                            val state = it.eCGConfiguration.copy(
                                state = ConnectionState.Connected, startEcgEnabled = true
                            )
                            it.copy(eCGConfiguration = state)
                        }
                        sendEvent(EcgEvents.StartRender(mEcgQueue))
                    }
                }

                override fun OnBluConnectionInterrupted() {
                    viewModelScope.launch {
                        mutableState.update {
                            val state = it.eCGConfiguration.copy(
                                state = ConnectionState.Interrupted,
                                startEcgEnabled = false,
                                isRendering = false,
                                connectEnabled = true
                            )
                            it.copy(eCGConfiguration = state)
                        }
                        sendEvent(EcgEvents.MsgInterrupted)
                    }
                }

                override fun OnBluConnectFaild() {
                    viewModelScope.launch {
                        mutableState.update {
                            val state = it.eCGConfiguration.copy(
                                state = ConnectionState.Failed, connectEnabled = true
                            )
                            it.copy(eCGConfiguration = state)

                        }
                     //   sendEvent(EcgEvents.ShowToast("Connect Failed"))
                    }
                }
            })
        data?.setDisplayMode(DataUtils.DISPLAY_MODE_12x1)
        data?.setGain(DataUtils.DISPLAY_GAIN_5)
        data?.setSpeed(DataUtils.DISPLAY_SPEED_25)
        delay(500)
        nativeMsg.setEventsChannel(actionChannelSend)//TODO find solution for this
        data?.gatherStart(nativeMsg)

        // data?.bindDev(address)
    }

    private fun stopBluetoothAndGathering() {
        if (bluetoothRepository != null) {
            bluetoothRepository.stopScan(object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)
                    Logger.e(TAG, "stopBluetoothAndGathering onScanResult")
                    data?.gatherEnd()
                }

                override fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                    Logger.e(TAG, "stopBluetoothAndGathering onScanFailed")

                }

                override fun onBatchScanResults(results: List<ScanResult?>?) {
                    super.onBatchScanResults(results)
                    Logger.e(TAG, "stopBluetoothAndGathering onBatchScanResults")

                }
            })
        }
    }


    private val jsonDecoder = Json { ignoreUnknownKeys = true } // Ignore unknown fields

    private val TAG = "EcgViewModel"
    fun checkBluetooth() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mutableState.value.shouldRequestBluetooth) {
                if (bluetoothRepository.isBluetoothEnabled()) {
                    val permissions = listOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                    val pairedDevices: Set<BluetoothDevice>? =
                        bluetoothRepository.getPairedDevices()
                    pairedDevices?.forEach { device ->
                        // Access device.name and device.address
                        if (devices.any { device.name.startsWith(it) }) {
                            init(device.address)
//                        sendEvent(EcgEvents.SendDeviceName(device.name))
                        }
                    }
                } else {
                    //you should enable bluetooth msg
                    sendEvent(EcgEvents.ShowToast("Please enable bluetooth"))
                }
            }

        }
    }

}