package com.example.kotlintest.screens.theremometer

import com.contec.htd.code.connect.ContecSdk
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.core.workers.Worker
import com.example.kotlintest.di.ThermometerQualifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class ThermometerState(
    val temp: Float = 33F,
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "Thermometer",
        titleIcon = R.drawable.ic_bluetooth_on,
        cancelIcon = R.drawable.ic_cancel,
        cancelText = "Cancel"
    ),
    val normalRange: String = "Normal 36.1 - 37.2",
    )

sealed class ThermometerAction {
    data class Bluetooth(val command: BluetoothCommand) : ThermometerAction()
}

sealed class ThermometerEvents {
    data class ShowMsg(val msg: String) : ThermometerEvents()

}

@HiltViewModel
class ThermometerViewModel @Inject constructor(
    private val sdk: ContecSdk,
    private val deviceManager: DeviceManager,
    @ThermometerQualifier private val worker: Worker
) :
    BaseViewModel<ThermometerState, ThermometerEvents, ThermometerAction>(
        initialState = ThermometerState()
    ) {

    private val TAG = "ThermometerViewModel"
    override fun handleAction(action: ThermometerAction) {
        when (action) {
            is ThermometerAction.Bluetooth -> {
                when (action.command) {
                    BluetoothCommand.SearchAndCommunicate -> connectToDevice()
                    BluetoothCommand.StopBluetoothAndCommunication -> disconnectToDevice()
                }
            }

        }

    }

    private fun disconnectToDevice() {
        worker.stopWork()
    }

    fun connectToDevice() {
        if (deviceManager.bluetoothScanner.isBluetoothEnabled()) {
            worker.startWork { result ->
                mutableState.update {
                    it.copy(temp = result.getString("temp").toFloat())
                }
            }

        } else {
            sendEvent(ThermometerEvents.ShowMsg("Please enable bluetooth"))
        }
    }

}