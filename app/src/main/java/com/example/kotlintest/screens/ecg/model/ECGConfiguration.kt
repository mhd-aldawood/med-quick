package com.example.kotlintest.screens.ecg.model

import com.example.kotlintest.core.model.ConnectionState
import com.example.kotlintest.screens.ecg.EcgEvents

data class ECGConfiguration(
    val state: ConnectionState = ConnectionState.Idle,
    val hrBpm: String? = null,
    val leadOffText: String? = null,
    val connectEnabled: Boolean = true,
    val startEcgEnabled: Boolean = false,
    val isRendering: Boolean = false,
    val hr: Int = 0,
    val leadOffFlag: String? = null,
    val saveProgress: Int = 0,
    val caseSaving: Boolean = false,
    val heartbeat: Boolean = false,
    val battery: Int = 0,
    val remainingSec: Int = 0,
    val waveStable: Boolean = false,
    val lastWaveSize: Int = 0,
    val nibp: NibpReading? = null,
    val spo2: SpO2Reading? = null,
    val steps: Int = 0,
    val turns: Int = 0,
    val stepsPercent: Int = 0
)
data class NibpReading(val sys: Int, val dia: Int, val mean: Int, val pr: Int, val err: Byte)
data class SpO2Reading(val spo2: Int, val pr: Int, val state: Byte)