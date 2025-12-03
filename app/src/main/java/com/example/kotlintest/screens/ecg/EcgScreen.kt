package com.example.kotlintest.screens.ecg

import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.screens.ecg.views.DurationWithIcon
import com.example.kotlintest.screens.ecg.views.EcgGraph
import com.example.kotlintest.ui.theme.locals.LocalReviewWaveController
import com.example.kotlintest.util.Logger

@Preview
@Composable
fun EcgScreen(viewModel: EcgViewModel = hiltViewModel(), uiState: EcgState = EcgState()) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current


    val controller = LocalReviewWaveController.current

    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                Logger.i("Ecg screen", " on destroy")
                controller.stopRender()//TODO  call this when we have a disconnect status of bluetooth
                controller.detach()
                viewModel.trySendAction(EcgAction.StopBluetoothAndGathering)
            }
            if (event == Lifecycle.Event.ON_START) {
                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    EventsEffect(viewModel) {
        when (it) {
            is EcgEvents.ShowToast -> {
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
            }

            is EcgEvents.SetRenderColor -> controller.setRenderColor()
            is EcgEvents.MsgInterrupted -> controller.stopRender()
            is EcgEvents.StartRender -> {
                controller.setEcgDataBuf()
                controller.startRenderer()
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.trySendAction(EcgAction.CheckBluetooth)
    }


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        EcgGraph(controller)
        DurationWithIcon(onDurationChange = {
            viewModel.trySendAction(EcgAction.OnDurationChange(it))
        }, uiState.checkIcon)


    }
}