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
import com.example.kotlintest.screens.theremometer.views.TemperatureCard

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



    LaunchedEffect(Unit) {
        viewModel.trySendAction(ThermometerAction.ConnectToDevice)
    }

    TemperatureCard(uiState.temp, uiState.normalRange)

}