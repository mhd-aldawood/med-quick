package com.example.kotlintest.screens.urineanalyzer

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.DeviceManager
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.core.model.CardResult
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.core.workers.Worker
import com.example.kotlintest.di.UrineAnalyzerQualifier
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.screens.urineanalyzer.models.UrineAnalyzerCardResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UrineAnalyzerViewModel @Inject constructor(
    @param:UrineAnalyzerQualifier val worker: Worker,
    val deviceManager: DeviceManager
) : BaseViewModel<UrineAnalyzerState, UrineAnalyzerEvents, UrineAnalyzerActions>(
        initialState = UrineAnalyzerState()
    ) {
    init {
        deviceManager.setDeviceModels(DeviceCategory.UrineAnalyzer)
    }

    override fun handleAction(action: UrineAnalyzerActions) {
        when (action) {
            is UrineAnalyzerActions.Bluetooth -> {
                when (action.command) {
                    BluetoothCommand.SearchAndCommunicate -> handleSearchAndCommunicate()
                    BluetoothCommand.StopBluetoothAndCommunication -> handleStopBluetoothAndCommunication()
                }
            }
        }
    }

    private fun handleStopBluetoothAndCommunication() {
        worker.stopWork()
    }

    private fun handleSearchAndCommunicate() {
        viewModelScope.launch(Dispatchers.IO) {
            if (deviceManager.bluetoothScanner.isBluetoothEnabled())
                worker.startWork() { result ->
                    viewModelScope.launch(Dispatchers.Default){
                        mutableState.update {
                            val cardResult= UrineAnalyzerCardResult(
                                id="1",
                                date = result.getString("date").toString()
                                ,cardResult = mutableListOf<CardResult>(
                                    CardResult(listOf("URO",  result.getString("URO"), "PH" ,result.getString("PH"))),
                                    CardResult(listOf("BLD", result.getString("BLD"), "NIT", result.getString("NIT"))),
                                    CardResult(listOf("BIL", result.getString("BIL"), "LEU", result.getString("LEU"))),
                                    CardResult(listOf("KET", result.getString("KET"), "SG", result.getString("SG"))),
                                    CardResult(listOf("GLU", result.getString("GLU"), "VC", result.getString("VC"))),
                                    CardResult(listOf("BRO", result.getString("PRO"), "", "")),
//                                   CardResult(listOf("MAL", result.getString("MAL"), "CR", result.getString("CR")))
//                                   CardResult(listOf("UCA", result.getString("UCA"), "", ""))
                                ))
                            it.copy(cardList=it.cardList+cardResult)
                        }
                    }
                }
        }

    }
}

sealed class UrineAnalyzerActions {
    data class Bluetooth(val command: BluetoothCommand) : UrineAnalyzerActions()
}

sealed class UrineAnalyzerEvents {

}

data class UrineAnalyzerState(
    val headerDataSection: HeaderDataSection = HeaderDataSection(
        title = "Urine Analyzer",
        titleIcon = R.drawable.ic_bluetooth_on
    ),
    val cardList: List<UrineAnalyzerCardResult> = mutableListOf<UrineAnalyzerCardResult>(
        UrineAnalyzerCardResult(
            id = "Test No:001", date = "2024/03026 14:07:13",
            cardResult = mutableListOf<CardResult>(
                CardResult(listOf("URO", "2", "PH", "4")),
                CardResult(listOf("BLD", "2", "NIT", "4")),
                CardResult(listOf("BIL", "2", "LEU", "4")),
                CardResult(listOf("KET", "2", "SG", "4")),
                CardResult(listOf("GLU", "2", "VC", "4")),
                CardResult(listOf("BRO", "2", "", ""))
            )
        ),
        UrineAnalyzerCardResult(
            "Test No:002", "2024/03026 14:07:13", mutableListOf<CardResult>(
                CardResult(listOf("URO", "2", "PH", "4")),
                CardResult(listOf("BLD", "2", "NIT", "4")),
                CardResult(listOf("BIL", "2", "LEU", "4")),
                CardResult(listOf("KET", "2", "SG", "4")),
                CardResult(listOf("GLU", "2", "VC", "4")),
                CardResult(listOf("BRO", "2", "", ""))
            )
        ),
    ),
    val cardHeader: List<String> = listOf("Item", "Result", "Item", "Result")
)
