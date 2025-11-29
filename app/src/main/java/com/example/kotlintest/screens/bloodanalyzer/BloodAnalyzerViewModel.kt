package com.example.kotlintest.screens.bloodanalyzer

import androidx.lifecycle.viewModelScope
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.core.workers.Worker
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.di.BloodAnalyzerQualifier
import com.example.kotlintest.screens.bloodanalyzer.models.WhiteBloodCellAnalyzerResult
import com.example.kotlintest.screens.bloodanalyzer.models.WhiteBloodCellAnalyzerValues
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.screens.pulseoximeter.models.PulseOximeterCard
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.util.CellResult
import com.example.kotlintest.util.createResultFromCell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
@HiltViewModel
class BloodAnalyzerViewModel @Inject constructor(
    private val deviceManager: DeviceManager,
    private @BloodAnalyzerQualifier val worker: Worker
) : BaseViewModel<BloodAnalyzerState, BloodAnalyzerEvents, BloodAnalyzerActions>(initialState = BloodAnalyzerState()) {
    private val TAG = "BloodAnalyzerViewModel"
//    val latestResult: StateFlow<CellResult?> = wbcDevice.latestResult
//    val connected: StateFlow<Boolean> = wbcDevice.connected
    init {
        deviceManager.setDeviceModels(DeviceCategory.WhiteBloodCellAnalyzer)
    }

    override fun handleAction(action: BloodAnalyzerActions) {
        when (action) {
            is BloodAnalyzerActions.Bluetooth -> {
                when (action.command) {
                    BluetoothCommand.SearchAndCommunicate -> {
                        startDiscovery()
                    }

                    BluetoothCommand.StopBluetoothAndCommunication -> {}
                }
            }

        }

    }

    fun startDiscovery() {
        viewModelScope.launch(Dispatchers.IO) {
            worker.startWork { jSONObject ->
                viewModelScope.launch(Dispatchers.Default) {
                    val obj = Json.decodeFromString<CellResult>(jSONObject.toString())
                    mutableState.update {
                        val newResult = WhiteBloodCellAnalyzerResult(
                            cardValues = createResultFromCell(obj),
                            date = obj.dateTime
                        )
                        it.copy(bloodCellAnalyzerResults = it.bloodCellAnalyzerResults + newResult)
                    }
                }

            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        worker.stopWork()
    }
}

data object BloodAnalyzerEvents
sealed class BloodAnalyzerActions() {
    data class Bluetooth(val command: BluetoothCommand) : BloodAnalyzerActions()
}

data class BloodAnalyzerState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "White Blood Cell Analyzer",
        titleIcon = R.drawable.ic_bluetooth_on
    ),
    val bloodCellAnalyzerResults: List<WhiteBloodCellAnalyzerResult> = mutableListOf<WhiteBloodCellAnalyzerResult>(
        WhiteBloodCellAnalyzerResult(
            cardValues = mutableListOf<WhiteBloodCellAnalyzerValues>(
                WhiteBloodCellAnalyzerValues(
                    item = "WBC",
                    result = "0.0",
                    units = "10^9/L",
                    reference = "3.6 - 10.0"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "LYM",
                    result = "0.0",
                    units = "%",
                    reference = "1.0 - 3.7"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "MON",
                    result = "0.0",
                    units = "%",
                    reference = "1.0 - 0.8"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "NEU",
                    result = "0.0",
                    units = "%",
                    reference = "1.5 - 7.0"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "EQS",
                    result = "0.0",
                    units = "%",
                    reference = "0.1 - 0.5"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "BAS",
                    result = "0.0",
                    units = "%",
                    reference = "0.0 - 0.1"
                ),
            )
        ),
        WhiteBloodCellAnalyzerResult(
            cardValues = mutableListOf<WhiteBloodCellAnalyzerValues>(
                WhiteBloodCellAnalyzerValues(
                    item = "WBC",
                    result = "0.0",
                    units = "10^9/L",
                    reference = "3.6 - 10.0"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "LYM",
                    result = "0.0",
                    units = "%",
                    reference = "1.0 - 3.7"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "MON",
                    result = "0.0",
                    units = "%",
                    reference = "1.0 - 0.8"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "NEU",
                    result = "0.0",
                    units = "%",
                    reference = "1.5 - 7.0"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "EQS",
                    result = "0.0",
                    units = "%",
                    reference = "0.1 - 0.5"
                ),
                WhiteBloodCellAnalyzerValues(
                    item = "BAS",
                    result = "0.0",
                    units = "%",
                    reference = "0.0 - 0.1"
                ),
            )
        )
    ),
    val pulseCard: PulseOximeterCard = PulseOximeterCard(
        value = "3.5",
        valueColor = FrenchWine,
        cardColor = PrimaryMidLinkColor,
        title = "WPC",
        cardUnit = "10^9/L",
        normalRange = "3.6-10"
    )
)
