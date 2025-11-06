package com.example.kotlintest.screens.stethoscope

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.screens.stethoscope.views.PickAuscultationSite
import com.example.kotlintest.screens.stethoscope.views.WaveHeart
import com.example.kotlintest.util.Logger

@Composable
fun StethoScopeScreen(viewModel: StethoScopeViewModel, uiState1: StethoScopeState) {

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                Logger.i("Tonometer screen", " on destroy")
                viewModel.trySendAction(StethoScopeAction.StopBluetoothAndCommunication)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
        WaveHeart()
    }
}

