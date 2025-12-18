package com.example.kotlintest.screens.urineanalyzer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.kotlintest.R
import com.example.kotlintest.component.DataCardSection
import com.example.kotlintest.core.bluetooth.BluetoothCommand
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor
import com.example.kotlintest.ui.theme.rhDisplayBold
import com.example.kotlintest.ui.theme.rhDisplayExtraBold
import com.example.kotlintest.ui.theme.rhDisplayRegular
import com.example.kotlintest.util.Logger
import com.example.kotlintest.util.horizontalPadding
import com.example.kotlintest.util.verticalPadding

@Composable
fun UrineAnalyzerScreen(
    onCheckClicked: () -> Unit,
    uiState: UrineAnalyzerState,
    viewModel: UrineAnalyzerViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                Logger.i("Tonometer screen", " on destroy")
                viewModel.trySendAction(UrineAnalyzerActions.Bluetooth(BluetoothCommand.StopBluetoothAndCommunication))
            }
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.trySendAction(UrineAnalyzerActions.Bluetooth(BluetoothCommand.SearchAndCommunicate))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(0.3f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(uiState.cardList) { it ->
                    DataCardSection(
                        cardHeader = uiState.cardHeader,
                        cardResult = it.cardResult,
                        id = it.id,
                        date = it.date,
                        idTextStyle = MaterialTheme.typography.rhDisplayExtraBold.copy(
                            fontSize = 18.sp,
                            color = PrimaryMidLinkColor
                        ),
                        dateTextStyle = MaterialTheme.typography.rhDisplayRegular.copy(
                            fontSize = 18.sp,
                            color = PrimaryMidLinkColor
                        ),
                        titleTextStyle = MaterialTheme.typography.rhDisplayBold.copy(
                            fontSize = 18.sp,
                            color = PrimaryMidLinkColor,
                            textAlign = TextAlign.Center
                        ),
                        bodyTextStyle = MaterialTheme.typography.rhDisplayRegular.copy(
                            fontSize = 18.sp,
                            color = PrimaryMidLinkColor,
                            textAlign = TextAlign.Center
                        ),
                        idRowModifier = Modifier.fillMaxWidth().horizontalPadding(15)
                    )

                }

            }
            Image(
                modifier = Modifier.weight(0.3f),
                painter = painterResource(R.drawable.ic_fia_testing_system__poct),
                contentDescription = ""
            )
        }
        Icon(
            painter = painterResource(R.mipmap.ic_check),
            modifier = Modifier
                .size(100.dp)
                .align(
                    Alignment.BottomEnd
                )
                .clickable { onCheckClicked.invoke() },
            contentDescription = "",
            tint = Color.Unspecified
        )

    }


}