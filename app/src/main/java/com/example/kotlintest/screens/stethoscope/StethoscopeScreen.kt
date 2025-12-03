package com.example.kotlintest.screens.stethoscope

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.screens.stethoscope.views.AudioWaveform
import com.example.kotlintest.screens.stethoscope.views.PickAuscultationSite
import com.example.kotlintest.screens.stethoscope.views.RecordAndButtons
import com.example.kotlintest.ui.theme.Periwinkle
import com.example.kotlintest.util.Logger

@Composable
fun StethoScopeScreen(
    viewModel: StethoScopeViewModel,
    uiState1: StethoScopeState,
    onCheckClicked: () -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                Logger.i("Tonometer screen", " on destroy")
                viewModel.trySendAction(StethoScopeAction.StopBluetoothAndCommunication)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    EventsEffect(viewModel) {
        when (it) {
            is StethoScopeEvents.NavigateBack -> onCheckClicked
            is StethoScopeEvents.ShowMsg -> Toast.makeText(context, it.msg, Toast.LENGTH_SHORT)
                .show()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.trySendAction(StethoScopeAction.SearchAndConnectToStethoScope)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        PickAuscultationSite(
            uiState1.pickLocation,
            uiState1.listOfAuscultationSite,
            onCardClicked = { index ->
                viewModel.trySendAction(
                    StethoScopeAction.ChangeAuscultationSite(index)
                )
            })
        HorizontalSpacer(30)
        AudioWaveform(
            samples = uiState1.heartValue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            barColor = Periwinkle,
            minBarHeight = 10.dp
        )
        HorizontalSpacer(40)
        RecordAndButtons(
            uiState1,
            onRecordClick = { viewModel.trySendAction(StethoScopeAction.RecordClick) },
            onCheckClick = { onCheckClicked },
            onPLayClick = { path, status ->
                viewModel.trySendAction(
                    StethoScopeAction.PlayRecord(
                        path,
                        status
                    )
                )
            },
            onDeleteClicked = { path, phase ->
                viewModel.trySendAction(
                    StethoScopeAction.DeleteRecord(
                        path,
                        phase
                    )
                )
            })
    }
}

