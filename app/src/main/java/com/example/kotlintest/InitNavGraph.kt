package com.example.kotlintest

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
import com.example.kotlintest.features_autentication.presentation.screens.AuthScreen
import com.example.kotlintest.features_appointment.presentation.screens.AppointmentCreateScreen
import com.example.kotlintest.features_home.presentation.screens.CalendarScreen
import com.example.kotlintest.features_splash.presentation.screens.SplashScreen
import com.example.kotlintest.navigation.navigateSelectedDevice
import com.example.kotlintest.navigation.navigateThroughTopBar
import com.example.kotlintest.navigation.navigateToCallScreen
import com.example.kotlintest.screens.bloodanalyzer.BloodAnalyzerScreen
import com.example.kotlintest.screens.bloodanalyzer.BloodAnalyzerViewModel
import com.example.kotlintest.screens.call.TwoColumnFrontCameraScreen
import com.example.kotlintest.screens.ecg.EcgScreen
import com.example.kotlintest.screens.ecg.EcgViewModel
import com.example.kotlintest.screens.home.ExaminationScreen
import com.example.kotlintest.screens.poct.PoctScreen
import com.example.kotlintest.screens.poct.PoctViewModel
import com.example.kotlintest.screens.pulseoximeter.PulseOximeterScreen
import com.example.kotlintest.screens.pulseoximeter.PulseOximeterViewModel
import com.example.kotlintest.screens.stethoscope.StethoScopeScreen
import com.example.kotlintest.screens.stethoscope.StethoScopeViewModel
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
    startDestination: String = NavDestination.CALENDAR_SCREEN
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(NavDestination.SPLASH_SCREEN) {
            SplashScreen(navController =navController )
        }
        composable(NavDestination.AUTH_SCREEN) {
            AuthScreen(navController =navController )
        }
        composable(NavDestination.APPOINTMENT_CREATE_SCREEN) {
            MainScaffold(
            ) {
                AppointmentCreateScreen(navController = navController)
            }
        }
        composable(NavDestination.CALENDAR_SCREEN) {
            MainScaffold(
                icons = listOf(
                    R.drawable.ic_med_calender,
                    R.drawable.ic_med_devices,
                    R.drawable.ic_med_examiniation,
                    R.drawable.ic_med_profile,
                    R.drawable.ic_med_settings
                ),
                titles =listOf(
                    "Calendar",
                    "Devices",
                    "Examination",
                    "Profile",
                    "Settings",
                ), onCenterIconClick = {index->
                    navController.navigateThroughTopBar(index)
                }
            ) {
                CalendarScreen(navController = navController)
            }
        }

        composable(NavDestination.EXAMINATION_SCREEN) {
            MainScaffold(
                icons = listOf(
                    R.drawable.ic_med_calender,
                    R.drawable.ic_med_devices,
                    R.drawable.ic_med_examiniation,
                    R.drawable.ic_med_profile,
                    R.drawable.ic_med_settings
                ),onCenterIconClick = {index->
                    navController.navigateThroughTopBar(index)
                }
            ) {
                ExaminationScreen(navigateToSelectedDevice = { selectedDevice ->
                    navController.navigateSelectedDevice(selectedDevice)
                }, onCallClicked = { navController.navigateToCallScreen() })
            }
        }
        composable(NavDestination.PULSE_OXIMETER_SCREEN) {
            val pulseOximeterViewModel: PulseOximeterViewModel = hiltViewModel()
            val uiState by pulseOximeterViewModel.stateFlow.collectAsStateWithLifecycle()
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
        composable(NavDestination.THERMOMETER_SCREEN) {
            val thermometerViewModel: ThermometerViewModel = hiltViewModel()
            val uiState by thermometerViewModel.stateFlow.collectAsStateWithLifecycle()
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
        composable(NavDestination.STETHOSCOPE_SCREEN) {
            val stethoScopeViewModel: StethoScopeViewModel = hiltViewModel()
            val uiState by stethoScopeViewModel.stateFlow.collectAsStateWithLifecycle()
            DeviceMainScreen(
                title = uiState.headerDataSection.title,
                titleIcon = uiState.headerDataSection.titleIcon,
                cancelIcon = uiState.headerDataSection.cancelIcon,
                cancelText = uiState.headerDataSection.cancelText,
                onCancelClick = { navController.popBackStack() },
            ) {
                StethoScopeScreen(
                    stethoScopeViewModel,
                    uiState,
                    onCheckClicked = { navController.popBackStack() })
            }
        }
        composable(NavDestination.POCT_SCREEN) {
            val poctViewModel: PoctViewModel = hiltViewModel()
            val uiState by poctViewModel.stateFlow.collectAsStateWithLifecycle()
            DeviceMainScreen(
                title = uiState.headerDataSection.title,
                titleIcon = uiState.headerDataSection.titleIcon,
                cancelIcon = uiState.headerDataSection.cancelIcon,
                cancelText = uiState.headerDataSection.cancelText,
                onCancelClick = { navController.popBackStack() },
            ) {
                PoctScreen(
                    poctViewModel,
                    uiState
                )
            }
        }
        composable(NavDestination.WHITE_BLOOD_CELL_ANALYZER_SCREEN) {
            val analyzerViewModel: BloodAnalyzerViewModel = hiltViewModel()
            val uiState by analyzerViewModel.stateFlow.collectAsStateWithLifecycle()
            DeviceMainScreen(
                title = uiState.headerDataSection.title,
                titleIcon = uiState.headerDataSection.titleIcon,
                cancelIcon = uiState.headerDataSection.cancelIcon,
                cancelText = uiState.headerDataSection.cancelText,
                onCancelClick = { navController.popBackStack() },
            ) {
                BloodAnalyzerScreen(
                    uiState, analyzerViewModel
                )
            }
        }
        composable(NavDestination.CALL_SCREEN) {
            DeviceMainScreen(
                title = "Camera Call",
                titleIcon = R.drawable.ic_online_call,
                cancelIcon = R.drawable.ic_cancel,
                cancelText = "Cancel",
                onCancelClick = { navController.popBackStack() },
            ) {
                TwoColumnFrontCameraScreen()
            }
        }
    }

}


@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun DefaultPreview() {
    KotlinTestTheme {
        InitNavGraph()
    }
}
