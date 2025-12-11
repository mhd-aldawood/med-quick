package com.example.kotlintest.screens.tonometer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.screens.tonometer.views.LampSelectionInfoCard
import com.example.kotlintest.screens.tonometer.views.PressureInfoCard
import com.example.kotlintest.util.Logger

@Composable
fun TonometerScreen(viewModel: TonometerViewModel, uiState: TonometerState) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                Logger.i("Tonometer screen", " on destroy")
                viewModel.trySendAction(TonometerAction.Bluetooth(BluetoothCommand.StopBluetoothAndCommunication))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.trySendAction(TonometerAction.Bluetooth(BluetoothCommand.SearchAndCommunicate))
    }



    Row(
        modifier = Modifier
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LampSelectionInfoCard(uiState,
            onLambChange = { chosenLamp -> viewModel.trySendAction(TonometerAction.OnLambChange(chosenLamp)) },
            onAgeGroupChange = {viewModel.trySendAction(TonometerAction.OnAgeGroupChange(it))},
            onSittingPosChange = {positionType->viewModel.trySendAction(TonometerAction.OnSittingPosChange(positionType))})
        PressureInfoCard(
            pressureValue = uiState.pressure,
            systolicPressure = uiState.systolicPressure,
            pulseRate = uiState.pulse,
            pressureIcon = uiState.pressureIcon
        )
    }

}



