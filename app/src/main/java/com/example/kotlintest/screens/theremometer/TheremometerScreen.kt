package com.example.kotlintest.screens.theremometer

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.screens.theremometer.views.TemperatureCard
import com.example.kotlintest.util.Logger

@Composable
fun ThermometerScreen(viewModel: ThermometerViewModel, uiState: ThermometerState) {
    val context = LocalContext.current


    EventsEffect(viewModel) {
        when (it) {
            is ThermometerEvents.ShowMsg -> {
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
            }

        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.trySendAction(ThermometerAction.Bluetooth(BluetoothCommand.StopBluetoothAndCommunication))
            }
            if (event == Lifecycle.Event.ON_START) {
                viewModel.trySendAction(ThermometerAction.Bluetooth(BluetoothCommand.SearchAndCommunicate))
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

    TemperatureCard(uiState.temp, uiState.normalRange)

}