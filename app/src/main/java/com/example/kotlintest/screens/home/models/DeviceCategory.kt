package com.example.kotlintest.screens.home.models

sealed class DeviceCategory() {
    object Thermometer : DeviceCategory()
    object FIATestingSystemPOCT : DeviceCategory()
    object Spirometer : DeviceCategory()
    object HemoglobinTestingSystem : DeviceCategory()
    object PulseOximeter : DeviceCategory()
    object Glucometer : DeviceCategory()
    object UrineAnalyzer : DeviceCategory()
    object DopplerUltrasound : DeviceCategory()
    object ECGWorkstation : DeviceCategory()
    object DigitalStethoscope : DeviceCategory()
    object WhiteBloodCellAnalyzer : DeviceCategory()
    object ElectronicSphygmomanometer : DeviceCategory()
    object LipidTestingSystem : DeviceCategory()
}