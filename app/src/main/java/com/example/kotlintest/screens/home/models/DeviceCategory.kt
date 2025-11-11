package com.example.kotlintest.screens.home.models

sealed class DeviceCategory(val title: String) {
    object Thermometer : DeviceCategory("Thermometer")
    object FIATestingSystemPOCT : DeviceCategory("FIATestingSystemPOCT")
    object Spirometer : DeviceCategory("Spirometer")
    object HemoglobinTestingSystem : DeviceCategory("HemoglobinTestingSystem")
    object PulseOximeter : DeviceCategory("PulseOximeter")
    object Glucometer : DeviceCategory("Glucometer")
    object UrineAnalyzer : DeviceCategory("UrineAnalyzer")
    object DopplerUltrasound : DeviceCategory("DopplerUltrasound")
    object ECGWorkstation : DeviceCategory("ECGWorkstation")
    object DigitalStethoscope : DeviceCategory("DigitalStethoscope")
    object WhiteBloodCellAnalyzer : DeviceCategory("WhiteBloodCellAnalyzer")
    object ElectronicSphygmomanometer : DeviceCategory("Electronic Sphygmomanometer")
    object LipidTestingSystem : DeviceCategory("LipidTestingSystem")
}