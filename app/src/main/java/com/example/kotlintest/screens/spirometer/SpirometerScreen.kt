package com.example.kotlintest.screens.spirometer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.screens.spirometer.views.DataCardSection
import com.example.kotlintest.screens.spirometer.views.IndicatorAndGraphSection
import com.example.kotlintest.util.Logger

@Composable
fun SpirometerScreen(viewModel: SpirometerViewModel, uiState: SpirometerState) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.trySendAction(SpirometerActions.Bluetooth(BluetoothCommand.StopBluetoothAndCommunication))
            }
            if (event == Lifecycle.Event.ON_START) {
                viewModel.trySendAction(SpirometerActions.Bluetooth(BluetoothCommand.SearchAndCommunicate))
            }
            if (event == Lifecycle.Event.ON_RESUME) {
                Logger.i("BloodAnalyzerScreen", "ON_RESUME")
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Row(modifier = Modifier.fillMaxWidth()){
        DataCardSection(uiState.cardHeader,uiState.cardResult)
        IndicatorAndGraphSection()
    }

}