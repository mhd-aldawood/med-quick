package com.example.kotlintest.screens.spirometer

import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.core.workers.Worker
import com.example.kotlintest.di.SpirometerQualifier
import com.example.kotlintest.screens.spirometer.models.SpirometerResult
import com.example.kotlintest.screens.theremometer.ThermometerEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpirometerViewModel @Inject constructor(
    @SpirometerQualifier val worker: Worker,
    val deviceManager: DeviceManager
) :
    BaseViewModel<SpirometerState, SpirometerEvents, SpirometerActions>(initialState = SpirometerState()) {
    override fun handleAction(action: SpirometerActions) {
        when (action) {
            is SpirometerActions.Bluetooth -> when (action.command) {
                BluetoothCommand.SearchAndCommunicate -> connectToDevice()
                BluetoothCommand.StopBluetoothAndCommunication -> disconnectDevice()
            }
        }
    }

    private fun disconnectDevice() {
        worker.stopWork()
    }

    private fun connectToDevice() {
        if (deviceManager.bluetoothScanner.isBluetoothEnabled()) {
            worker.startWork {

            }
        } else {
            sendEvent(SpirometerEvents.ShowMsg("Please enable bluetooth"))

        }

    }

}

sealed class SpirometerActions() {
    data class Bluetooth(val command: BluetoothCommand) : SpirometerActions()

}

sealed class SpirometerEvents() {
    class ShowMsg(val msg: String): SpirometerEvents()
}

data class SpirometerState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "Spirometer",
        titleIcon = R.drawable.ic_bluetooth_on
    ),
    val cardHeader: List<String> = listOf("Par", "Act", "Pre%"),
    val cardResult: List<SpirometerResult> = mutableListOf<SpirometerResult>(
        SpirometerResult(par = "FVC", act = "0.59 L", pre = "16 %"),
        SpirometerResult(par = "FEV1", act = "0.59 L", pre = "18 %"),
        SpirometerResult(par = "PEF", act = "2.93 L/S", pre = "41 %"),
        SpirometerResult(par = "FEV1/FVC", act = "100.00%", pre = "117  %"),
        SpirometerResult(par = "FEV6", act = "0.59 L", pre = "--"),
        SpirometerResult(par = "FEF25", act = " 2.99 L/S", pre = "48 %"),
        SpirometerResult(par = "FEF50", act = "2.61 L/S", pre = "57 %"),
        SpirometerResult(par = "FEF75", act = "1.48 L/S", pre = "65 %"),
        SpirometerResult(par = "FEF2575", act = "2.58 L/S", pre = "61 %"),
    )
)