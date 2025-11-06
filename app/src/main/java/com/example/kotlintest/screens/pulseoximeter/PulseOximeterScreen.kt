package com.example.kotlintest.screens.pulseoximeter

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.kotlintest.component.HorizontalSpacer
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.screens.pulseoximeter.views.PulseOximeterElevatedCard

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

    LaunchedEffect(Unit) {
        viewModel.trySendAction(PulseOximeterAction.ConnectToDevice)
    }

    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        uiState.pulseOximeterCardList.forEachIndexed { index, it ->
            PulseOximeterElevatedCard(
                value = it.value,
                unit = it.cardUnit,
                cardGeneralColor = it.cardColor,
                title = it.title,
                normalRange= it.normalRange
            )
            if (index == uiState.pulseOximeterCardList.size - 2) {
                HorizontalSpacer(80)
            }
        }
    }

}
