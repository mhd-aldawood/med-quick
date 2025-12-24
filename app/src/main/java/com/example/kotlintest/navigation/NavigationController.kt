package com.example.kotlintest.navigation

import androidx.navigation.NavController
import com.example.kotlintest.NavDestination
import com.example.kotlintest.NavDestination.CALL_SCREEN
import com.example.kotlintest.NavDestination.ECG_SCREEN
import com.example.kotlintest.NavDestination.POCT_SCREEN
import com.example.kotlintest.NavDestination.PULSE_OXIMETER_SCREEN
import com.example.kotlintest.NavDestination.STETHOSCOPE_SCREEN
import com.example.kotlintest.NavDestination.THERMOMETER_SCREEN
import com.example.kotlintest.NavDestination.TONOMETER_SCREEN
import com.example.kotlintest.NavDestination.WHITE_BLOOD_CELL_ANALYZER_SCREEN
import com.example.kotlintest.R
import com.example.kotlintest.features_home.presentation.data.model.Appointments
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

        DeviceCategory.UrineAnalyzer -> TODO()
        DeviceCategory.WhiteBloodCellAnalyzer -> safeNavigate(WHITE_BLOOD_CELL_ANALYZER_SCREEN)
    }
}
fun NavController.navigateToCallScreen() {
    safeNavigate(CALL_SCREEN)
}
fun NavController.navigateThroughTopBar(index: Int,appointments:String?=null) {//TODO change later this is mistake appointments
    when(index){
        R.drawable.ic_med_calender -> safeNavigate(NavDestination.CALENDAR_SCREEN)
        R.drawable.ic_med_devices->{}
        R.drawable.ic_med_examiniation->safeNavigate("${NavDestination.EXAMINATION_SCREEN}/${appointments}")
        R.drawable.ic_med_profile->{}
        R.drawable.ic_med_settings->{}
    }
}
fun NavController.navigateToExamination(index: Int) {
    safeNavigate("${NavDestination.EXAMINATION_SCREEN}/${index}")
}
