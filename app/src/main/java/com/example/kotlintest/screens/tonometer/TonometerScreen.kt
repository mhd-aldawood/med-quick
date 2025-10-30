package com.example.kotlintest.screens.tonometer

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.screens.tonometer.screencomponents.LampSelectionInfoCard
import com.example.kotlintest.screens.tonometer.screencomponents.PressureInfoCard
import com.example.kotlintest.util.Logger

@Composable
fun TonometerScreen(viewModel: TonometerViewModel, uiState: TonometerState) {
//    val sdk = LocalContecSdk.current Todo keep this line tell we decide what will do for the sdk for tonometer and the context


    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                Logger.i("Tonometer screen", " on destroy")
                viewModel.trySendAction(TonometerAction.StopBluetoothAndCommunication)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
            viewModel.trySendAction(TonometerAction.BluetoothRequestFinished)
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
            is TonometerEvents.ShowMsg -> {
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
        viewModel.trySendAction(TonometerAction.CheckBluetooth)
    }



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp, bottom = 50.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LampSelectionInfoCard(uiState,
            onLambChange = { chosenLamp -> viewModel.trySendAction(TonometerAction.OnLambChange(chosenLamp)) },
            onAgeGroupChange = {viewModel.trySendAction(TonometerAction.OnAgeGroupChange(it))},
            onSittingPosChange = {positionType->viewModel.trySendAction(TonometerAction.OnSittingPosChange(positionType))})
        PressureInfoCard(
            pressureValue = uiState.pressureValue,
            pressureIcon = uiState.pressureIcon,
            systolicPressure = uiState.systolicPressure,
            pulseRate = uiState.pulseRate
        )
    }

}



