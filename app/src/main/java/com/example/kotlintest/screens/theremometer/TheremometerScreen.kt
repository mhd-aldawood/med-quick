package com.example.kotlintest.screens.theremometer

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.screens.theremometer.screencomponent.TemperatureCard

@Composable
fun ThermometerScreen(viewModel: ThermometerViewModel, uiState: ThermometerState) {
    val context = LocalContext.current

    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    )
    { permissionsMap ->
        // Check individual permission results in the map
        if (permissionsMap[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
            permissionsMap[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        ) {
            // All permissions granted
            viewModel.trySendAction(ThermometerAction.BluetoothRequestFinished)
        } else {
            Toast.makeText(context, "Please grant location permissions", Toast.LENGTH_SHORT).show()
        }
    }
    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    )
    { permissionsMap ->
        if (permissionsMap[android.Manifest.permission.BLUETOOTH_CONNECT] == true &&
            permissionsMap[android.Manifest.permission.BLUETOOTH_SCAN] == true
        ) {
            Toast.makeText(context, "Bluetooth Enabled ✅", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Bluetooth not enabled ❌", Toast.LENGTH_SHORT).show()
        }
    }


    EventsEffect(viewModel) {
        when (it) {
            is ThermometerEvents.ShowMsg -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    enableBluetoothLauncher.launch(
                        arrayOf(
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN
                        )
                    )
                }
            }

        }
    }

    LaunchedEffect(uiState.shouldRequestBluetooth) {
        if (uiState.shouldRequestBluetooth)
            multiplePermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
    }

    LaunchedEffect(Unit) {
        viewModel.trySendAction(ThermometerAction.CheckBluetooth)
    }

    TemperatureCard(uiState.temp, uiState.normalRange)

}