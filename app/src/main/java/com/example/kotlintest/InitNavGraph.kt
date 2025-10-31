package com.example.kotlintest

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlintest.component.DeviceMainScreen
import com.example.kotlintest.component.MainScaffold
import com.example.kotlintest.core.PermissionGate
import com.example.kotlintest.core.PermissionGateSimple
import com.example.kotlintest.navigation.navigateSelectedDevice
import com.example.kotlintest.screens.ecg.EcgScreen
import com.example.kotlintest.screens.ecg.EcgViewModel
import com.example.kotlintest.screens.pulseoximeter.PulseOximeterScreen
import com.example.kotlintest.screens.home.HomeScreen
import com.example.kotlintest.screens.pulseoximeter.PulseOximeterViewModel
import com.example.kotlintest.screens.theremometer.ThermometerScreen
import com.example.kotlintest.screens.theremometer.ThermometerViewModel
import com.example.kotlintest.screens.tonometer.TonometerScreen
import com.example.kotlintest.screens.tonometer.TonometerViewModel
import com.example.kotlintest.ui.theme.KotlinTestTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun InitNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = NavDestination.HOME_SCREEN
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(NavDestination.HOME_SCREEN) {
            MainScaffold(
                icons = listOf(
                    R.drawable.ic_med_home,
                    R.drawable.ic_med_devices,
                    R.drawable.ic_med_examiniation,
                    R.drawable.ic_med_profile,
                    R.drawable.ic_med_settings
                )
            ) {
                HomeScreen(navigateToSelectedDevice = { selectedDevice ->
                    navController.navigateSelectedDevice(selectedDevice)
                })
            }

        }
        composable(NavDestination.PULSE_OXIMETER_SCREEN) {
            val pulseOximeterViewModel: PulseOximeterViewModel = hiltViewModel()
            val uiState by pulseOximeterViewModel.stateFlow.collectAsStateWithLifecycle()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PermissionGate(
                    permissions = listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    DeviceMainScreen(
                        title = uiState.headerDataSection.title,
                        titleIcon = uiState.headerDataSection.titleIcon,
                        cancelIcon = uiState.headerDataSection.cancelIcon,
                        cancelText = uiState.headerDataSection.cancelText,
                        onCancelClick = { navController.popBackStack() },
                    ) {
                        PulseOximeterScreen(pulseOximeterViewModel, uiState)
                    }
                }
            }


        }
        composable(NavDestination.THERMOMETER_SCREEN) {
            val thermometerViewModel: ThermometerViewModel = hiltViewModel()
            val uiState by thermometerViewModel.stateFlow.collectAsStateWithLifecycle()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PermissionGate(
                    permissions = listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    DeviceMainScreen(
                        title = uiState.headerDataSection.title,
                        titleIcon = uiState.headerDataSection.titleIcon,
                        cancelIcon = uiState.headerDataSection.cancelIcon,
                        cancelText = uiState.headerDataSection.cancelText,
                        onCancelClick = { navController.popBackStack() },
                    ) {
                        ThermometerScreen(thermometerViewModel, uiState)
                    }
                }
            }
        }
        composable(NavDestination.TONOMETER_SCREEN) {
            val tonometerViewModel: TonometerViewModel = hiltViewModel()
            val uiState by tonometerViewModel.stateFlow.collectAsStateWithLifecycle()
            DeviceMainScreen(
                title = uiState.headerDataSection.title,
                titleIcon = uiState.headerDataSection.titleIcon,
                cancelIcon = uiState.headerDataSection.cancelIcon,
                cancelText = uiState.headerDataSection.cancelText,
                onCancelClick = { navController.popBackStack() },
            ) {
                TonometerScreen(tonometerViewModel, uiState)
            }
        }
        composable(NavDestination.ECG_SCREEN) {
            val ecgViewModel: EcgViewModel = hiltViewModel()
            val uiState by ecgViewModel.stateFlow.collectAsStateWithLifecycle()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PermissionGate(
                    permissions = listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) {
                    DeviceMainScreen(
                        title = uiState.headerDataSection.title,
                        titleIcon = uiState.headerDataSection.titleIcon,
                        cancelIcon = uiState.headerDataSection.cancelIcon,
                        cancelText = uiState.headerDataSection.cancelText,
                        onCancelClick = { navController.popBackStack() },
                    ) {
                        EcgScreen(ecgViewModel, uiState)
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KotlinTestTheme {
        InitNavGraph()
    }
}
