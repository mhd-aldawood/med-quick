package com.example.kotlintest.screens.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kotlintest.component.VerticalSpacer
import com.example.kotlintest.core.EventsEffect
import com.example.kotlintest.screens.home.models.DeviceCategory
import com.example.kotlintest.screens.home.views.AddDeviceWithPatientInfo
import com.example.kotlintest.screens.home.views.DeviceListSection
import com.example.kotlintest.screens.home.views.DevicesSection
import com.example.kotlintest.util.PermissionManager.checkPermissions

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSelectedDevice: (DeviceCategory) -> Unit,
    onCallClicked: () -> Unit
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()
    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val context = LocalContext.current
    val permissionsGranted = remember { mutableStateOf(false) }
    val deviceCategory =
        remember { mutableStateOf<DeviceCategory?>(null) }//TODO make state for this in viewmodel

    // Initialize the launcher using rememberLauncherForActivityResult
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result ->
            permissionsGranted.value = result.all { it.value }
            // After permissions are granted, navigate if a device is selected
            if (permissionsGranted.value) {
                deviceCategory.value?.let { navigateToSelectedDevice(it) }
            }
        }
    )

    // Check if permissions are granted on first launch and update state
    LaunchedEffect(Unit) {
        permissionsGranted.value = checkPermissions(context, permissions)
    }

    // Call EventsEffect and handle permission checks and request
    EventsEffect(viewModel) { events ->
        when (events) {
            is HomeEvents.SelectedDevice -> {
                // Store the selected device category
                deviceCategory.value = events.deviceCategory

                // If permissions are granted, navigate immediately
                if (permissionsGranted.value) {
                    navigateToSelectedDevice(events.deviceCategory)
                } else {
                    // Request permissions if not granted, and navigation will be handled in onResult
                    permissionsLauncher.launch(permissions.toTypedArray())
                }
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
                .wrapContentWidth(),
            horizontalAlignment = Alignment.End
        ) {
            AddDeviceWithPatientInfo(
                onAddDeviceClicked = {},
                onCallClicked = { onCallClicked.invoke() },
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

