package com.example.kotlintest.screens.pulseoximeter

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.component.NormalRangeCard
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.util.Logger

@Composable
fun PulseOximeterScreen(viewModel: PulseOximeterViewModel, uiState: PulseOximeterState) {
    val context = LocalContext.current

    EventsEffect(viewModel) {
        when (it) {
            is PulseOximeterEvents.ShowMsg -> {
                Toast.makeText(context,it.msg, Toast.LENGTH_SHORT).show()
            }

        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                Logger.i("PulseOximeter screen", " on destroy")
                viewModel.trySendAction(PulseOximeterAction.Bluetooth(BluetoothCommand.StopBluetoothAndCommunication))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.trySendAction(PulseOximeterAction.Bluetooth(BluetoothCommand.SearchAndCommunicate))
    }

    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        uiState.pulseOximeterCardList.forEachIndexed { index, it ->
            NormalRangeCard(
                value = it.value,
                unit = it.cardUnit,
                cardGeneralColor = it.cardColor,
                title = it.title,
                normalRange = it.normalRange,
                boxModifier = Modifier
                    .width(300.dp)
            )
            if (index == uiState.pulseOximeterCardList.size - 2) {
                HorizontalSpacer(80)
            }
        }
    }

}
