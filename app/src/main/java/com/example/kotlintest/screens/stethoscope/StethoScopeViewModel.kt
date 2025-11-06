package com.example.kotlintest.screens.stethoscope

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.audio.AudioProcessor
import com.example.kotlintest.core.bluetooth.BluetoothScanner
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.screens.home.DeviceCategory
import com.example.kotlintest.screens.stethoscope.model.AuscultationSite
import com.example.kotlintest.ui.theme.CeruleanBlue
import com.example.kotlintest.ui.theme.PaleCerulean
import com.example.kotlintest.util.Logger
import com.kl.minttisdk.ble.BleManager
import com.kl.minttisdk.ble.callback.IAudioDataCallback
import com.kl.minttisdk.ble.callback.IBleConnectStatusListener
import com.kl.minttisdk.ble.callback.ISetAudioSwitchListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StethoScopeViewModel @Inject constructor(
    private val bluetoothScanner: BluetoothScanner,
    val deviceManager: DeviceManager,
    val audioProcessor: AudioProcessor
) : BaseViewModel<StethoScopeState, StethoScopeEvents, StethoScopeAction>(
    initialState = StethoScopeState()
) {

    init {
        deviceManager.setDeviceModels(DeviceCategory.DigitalStethoscope)
    }

    val iBleConnectStatusListener = object : IBleConnectStatusListener {
        override fun onConnectFail(p0: String?, p1: Int) {
            Logger.i(TAG, "onConnectFail: $p0")
            audioProcessor.stopCapture()
        }

        override fun onConnectSuccess(p0: String?) {
            Logger.i(TAG, "onConnectSuccess: $p0")
        }

        override fun onUpdateParamsSuccess() {
            BleManager.getInstance().notifyAudioData()
            audioProcessor.startCapture()
            Logger.i(TAG, "onUpdateParamsSuccess: ")
        }

        override fun onUpdateParamsFail() {
            Logger.i(TAG, "onUpdateParamsFail: ")
        }

        override fun onDisConnected(p0: String?, p1: Boolean, p2: Int) {
            Logger.i(TAG, "onDisConnected: {p0} {p2}")
            audioProcessor.stopCapture()
        }
    }

    override fun handleAction(action: StethoScopeAction) {
        when (action) {
            StethoScopeAction.SearchAndConnectToStethoScope -> searchAndConnectToStethoScope()
            StethoScopeAction.StopBluetoothAndCommunication -> {
                BleManager.getInstance().unNotifyAudioData()
                BleManager.getInstance().removeConnectionListener(iBleConnectStatusListener)
                audioProcessor.stopCapture()
            }

            is StethoScopeAction.ChangeAuscultationSite -> changeAuscultationSite(action.index)
        }
    }

    private fun changeAuscultationSite(index: Int) {
        mutableState.update { stethoScopeState ->
            val updatedList =
                stethoScopeState.listOfAuscultationSite.mapIndexed { auscultationSiteIndex, it ->
                    if (auscultationSiteIndex == index) {
                        it.copy(textColor = Color.White, backGroundColor = CeruleanBlue)
                    } else {
                        it.copy(textColor = PaleCerulean, backGroundColor = Color.Transparent)
                    }
                }
            stethoScopeState.copy(listOfAuscultationSite = updatedList)

        }
    }

    private fun connectToDevice(mac: String) {

        BleManager.getInstance().addConnectionListener(iBleConnectStatusListener)


        BleManager.getInstance().setAudioDataCallback(object : IAudioDataCallback {
            override fun onSpkData(p0: ShortArray?) {
                Logger.i(TAG, "onSpkData: $p0")
            }

            override fun onMicData(p0: ShortArray?) {
                Logger.i(TAG, "onMicData: $p0")
            }

            override fun onProcessData(p0: ShortArray?) {
                Logger.i(TAG, "onProcessData: $p0")
                audioProcessor?.onProcessData(p0)


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

    private val TAG = "StethoScopeViewModel"
    private fun searchAndConnectToStethoScope() {

        viewModelScope.launch(Dispatchers.IO) {
            if (mutableState.value.shouldRequestBluetooth) {
                if (deviceManager.bluetoothRepositoryImpl.isBluetoothEnabled()) {

                    bluetoothScanner.startDiscovery { devices ->
                        // This block runs AFTER discovery completes
                        devices.forEach { (name, mac) ->
                            if (deviceManager.getDeviceModels()
                                    .any { name.startsWith(it) }
                            ) {
                                Logger.i(TAG, "Device Found: ${name}  ${mac}")
                                connectToDevice(mac)
                            }
                        }
                    }


                }
            }
        }
    }
}

data class StethoScopeState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "Digital Stethoscope", titleIcon = R.drawable.ic_bluetooth_on
    ),
    val shouldRequestBluetooth: Boolean = true,
    val pickLocation: String = "Pick Auscultation Site",
    val listOfAuscultationSite: List<AuscultationSite> = listOf<AuscultationSite>(
        AuscultationSite(
            text = "Structure of cardiac auscultation aortic area ( body structure)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Structure of cardiac auscultation pulmonic area ( body structure)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Structure of cardiac auscultation tricuspid area ( body structure)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Structure of cardiac auscultation mitral area ( body structure)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Anterior upper lung field (2nd ICS, MCL)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Anterior lower lung field (4th ICS, MCL)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Posterior upper lung field (T3 paraspinal)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Posterior lower lung field (T7 paraspinal)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Right mid-axillary lung field (5th ICS)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
        AuscultationSite(
            text = "Left mid-axillary lung field (5th ICS)",
            textColor = PaleCerulean,
            backGroundColor = Color.Transparent
        ),
    ),
    val recordIcon: Int = R.mipmap.ic_stethoscope_record,
    val checkIcon: Int = R.mipmap.ic_check,
    val deleteIcon: Int = R.mipmap.ic_delete,
    val playIcon: Int = R.drawable.ic_play,
)

sealed class StethoScopeEvents {

}

sealed class StethoScopeAction {
    data object SearchAndConnectToStethoScope : StethoScopeAction()
    data object StopBluetoothAndCommunication : StethoScopeAction()
    data class ChangeAuscultationSite(val index: Int) : StethoScopeAction()
}
