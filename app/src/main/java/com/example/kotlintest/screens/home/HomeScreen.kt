package com.example.kotlintest.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kotlintest.component.VerticalSpacer
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.screens.home.views.AddDeviceWithPatientInfo
import com.example.kotlintest.screens.home.views.DeviceListSection
import com.example.kotlintest.screens.home.views.DevicesSection
import com.example.kotlintest.ui.theme.locals.LocalPermissionManager

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSelectedDevice: (DeviceCategory) -> Unit
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()
    val permissionManager = LocalPermissionManager.current

    EventsEffect(viewModel) { events ->
        when (events) {
            is HomeEvents.SelectedDevice -> {
                navigateToSelectedDevice(events.deviceCategory)
            }
        }

    }
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            DeviceListSection(
                uiState.dataHolder.deviceConfigure.deviceList,
                uiState.dataHolder.deviceConfigure.savedLocallyIcon,
                uiState.dataHolder.deviceConfigure.savedLocally,
                uiState.dataHolder.deviceConfigure.options,
                optionBtnClicked = { index ->
                }
            )
            VerticalSpacer(60)

            DevicesSection(uiState.dataHolder.cardList, onCardClick = { i ->
                viewModel.trySendAction(
                    HomeAction.OnCardClick(i)
                )
            })
        }
        Column(
            modifier = Modifier
                .wrapContentWidth()
        ) {
            AddDeviceWithPatientInfo(
                onAddDeviceClicked = {},
                uiState.dataHolder.patentInfo.age,
                uiState.dataHolder.patentInfo.gender,
                uiState.dataHolder.patentInfo.insuranceCompany,
                uiState.dataHolder.deviceConfigure.addDevices,
                uiState.dataHolder.deviceConfigure.addDevicesIcon,
                uiState.dataHolder.patentInfo.icon,
                uiState.dataHolder.patentInfo.name,
                uiState.dataHolder.patentInfo.insuranceInfo.companyName,
                uiState.dataHolder.patentInfo.insuranceInfo.insuranceType,
                uiState.dataHolder.patentInfo.insuranceInfo.number
            )
        }

    }


}
