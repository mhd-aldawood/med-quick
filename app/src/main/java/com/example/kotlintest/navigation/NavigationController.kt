package com.example.kotlintest.navigation

import androidx.navigation.NavController
import com.example.kotlintest.NavDestination.CALL_SCREEN
import com.example.kotlintest.NavDestination.ECG_SCREEN
import com.example.kotlintest.NavDestination.POCT_SCREEN
import com.example.kotlintest.NavDestination.PULSE_OXIMETER_SCREEN
import com.example.kotlintest.NavDestination.STETHOSCOPE_SCREEN
import com.example.kotlintest.NavDestination.THERMOMETER_SCREEN
import com.example.kotlintest.NavDestination.TONOMETER_SCREEN
import com.example.kotlintest.NavDestination.URINE_ANALYZER_SCREEN
import com.example.kotlintest.NavDestination.WHITE_BLOOD_CELL_ANALYZER_SCREEN
import com.example.kotlintest.screens.home.models.DeviceCategory
fun NavController.safeNavigate(
    destination: String
) {

    fun extractBeforeQuestionMark(input: String?): String {
        return input?.substringBefore("?") ?: ""
    }

    // Check if the current route is not the same as the destination
    if (extractBeforeQuestionMark(currentBackStackEntry?.destination?.route) != extractBeforeQuestionMark(
            destination
        )
    ) {
        navigate(destination)
    }
}

fun NavController.navigateSelectedDevice(deviceCategory: DeviceCategory) {
    when (deviceCategory) {
        DeviceCategory.DigitalStethoscope -> safeNavigate(STETHOSCOPE_SCREEN)
        DeviceCategory.DopplerUltrasound -> TODO()
        DeviceCategory.ECGWorkstation -> safeNavigate(ECG_SCREEN)
        DeviceCategory.ElectronicSphygmomanometer -> safeNavigate(TONOMETER_SCREEN)
        DeviceCategory.FIATestingSystemPOCT -> safeNavigate(POCT_SCREEN)
        DeviceCategory.Glucometer -> TODO()
        DeviceCategory.HemoglobinTestingSystem -> TODO()
        DeviceCategory.LipidTestingSystem -> TODO()
        DeviceCategory.PulseOximeter -> safeNavigate(PULSE_OXIMETER_SCREEN)
        DeviceCategory.Spirometer -> TODO()
        DeviceCategory.Thermometer -> safeNavigate(THERMOMETER_SCREEN)
        DeviceCategory.UrineAnalyzer -> safeNavigate(URINE_ANALYZER_SCREEN)
        DeviceCategory.WhiteBloodCellAnalyzer -> safeNavigate(WHITE_BLOOD_CELL_ANALYZER_SCREEN)
    }
}
fun NavController.NavigateToCallScreen() {
    safeNavigate(CALL_SCREEN)
}