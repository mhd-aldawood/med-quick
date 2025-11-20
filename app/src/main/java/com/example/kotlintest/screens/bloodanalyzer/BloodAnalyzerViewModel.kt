package com.example.kotlintest.screens.bloodanalyzer

import com.example.kotlintest.R
import com.example.kotlintest.core.BaseViewModel
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.core.model.HeaderDataSection
import com.example.kotlintest.screens.bloodanalyzer.models.WhiteBloodCellAnalyzerResult
import com.example.kotlintest.screens.bloodanalyzer.models.WhiteBloodCellAnalyzerValues
import com.example.kotlintest.screens.pulseoximeter.models.PulseOximeterCard
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BloodAnalyzerViewModel @Inject constructor() :
    BaseViewModel<BloodAnalyzerState, BloodAnalyzerEvents, BloodAnalyzerActions>(initialState = BloodAnalyzerState()) {
    private val TAG = "BloodAnalyzerViewModel"

    override fun handleAction(action: BloodAnalyzerActions) {

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
