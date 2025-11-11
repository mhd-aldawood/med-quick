package com.example.kotlintest.core.devicesWorker

import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.audio.AudioProcessor
import com.example.kotlintest.core.model.ConnectionState
import com.example.kotlintest.util.Logger
import com.kl.minttisdk.ble.BleManager
import com.kl.minttisdk.ble.callback.IAudioDataCallback
import com.kl.minttisdk.ble.callback.IBleConnectStatusListener
import com.kl.minttisdk.ble.callback.ISetAudioSwitchListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class StethoScopeWorker @Inject constructor(
    val deviceManager: DeviceManager,
    val audioProcessor: AudioProcessor
) {

    private val _spkChannel = Channel<ShortArray>(capacity = 1024)
    val spkFlow: Flow<ShortArray> = _spkChannel.receiveAsFlow()
    private val TAG = "StethoScopeWorker"
    private var startRecord: Boolean = false
    val iBleConnectStatusListener = object : IBleConnectStatusListener {
        override fun onConnectFail(p0: String?, p1: Int) {
            Logger.i(TAG, "onConnectFail: $p0")
            if (startRecord)
                audioProcessor.stopCapture()
            deviceManager.setConnectionState(ConnectionState.Failed)

        }

        override fun onConnectSuccess(p0: String?) {
            deviceManager.setConnectionState(ConnectionState.Connecting)

            Logger.i(TAG, "onConnectSuccess: $p0")
        }

        override fun onUpdateParamsSuccess() {
            BleManager.getInstance().notifyAudioData()
            deviceManager.setConnectionState(ConnectionState.Connected)

            if (startRecord)
                audioProcessor.startCapture()
            Logger.i(TAG, "onUpdateParamsSuccess: ")
        }

        override fun onUpdateParamsFail() {
            Logger.i(TAG, "onUpdateParamsFail: ")
            deviceManager.setConnectionState(ConnectionState.Failed)
        }

        override fun onDisConnected(p0: String?, p1: Boolean, p2: Int) {
            Logger.i(TAG, "onDisConnected: {p0} {p2}")
            deviceManager.setConnectionState(ConnectionState.Disconnecting)
            if (startRecord)
                audioProcessor.stopCapture()
        }
    }

    fun connect(mac: String) {
        BleManager.getInstance().addConnectionListener(iBleConnectStatusListener)
        BleManager.getInstance().setAudioDataCallback(object : IAudioDataCallback {
            override fun onSpkData(p0: ShortArray?) {
                Logger.i(TAG, "onSpkData: $p0")
            }

            override fun onMicData(p0: ShortArray?) {
            }

            override fun onProcessData(p0: ShortArray?) {
                Logger.i(TAG, "onProcessData: $p0")
                val copy: ShortArray? = p0?.copyOf() // BLE libs often reuse buffers
                copy?.let {
                    if (_spkChannel.trySend(copy).isFailure) {
                        _spkChannel.tryReceive() // drop one oldest
                        _spkChannel.trySend(copy)
                    }
                    if (startRecord)
                        audioProcessor.onProcessData(copy)
                }
            }

            override fun onHeartRate(p0: Int) {
                Logger.i(TAG, "onHeartRate: $p0")
            }

        }) //MHD this one
        BleManager.getInstance().readSetAudio(object : ISetAudioSwitchListener {
            override fun onSetAudioModeSwitch(p0: Int) {
                Logger.i(TAG, "onSetAudioModeSwitch: $p0")
            }

        }) //MHD this one
        BleManager.getInstance().connect(mac)
    }

    fun disconnect(release: Boolean = false) {
        BleManager.getInstance().unNotifyAudioData()
        BleManager.getInstance().removeConnectionListener(iBleConnectStatusListener)
        stopRecording(release)
    }

    fun startRecording() {
        startRecord = true
        audioProcessor.startCapture()
    }

    fun stopRecording(release: Boolean = false) {
        startRecord = false
        audioProcessor.stopCapture(release)

    }

}