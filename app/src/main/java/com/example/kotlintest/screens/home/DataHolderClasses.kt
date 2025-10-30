package com.example.kotlintest.screens.home

import androidx.compose.runtime.mutableStateListOf
import com.example.kotlintest.R


data class DataHolder(
    val deviceConfigure: DeviceConfigure = DeviceConfigure(),
    val patentInfo: PatentInfo = PatentInfo(),
    val cardList: List<HomeScreenCard> = listOf(
        HomeScreenCard(
            deviceIcon = R.drawable.ic_fia_testing_system__poct,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.CABLE, ConnectionType.Status.CONNECTED),
                ConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED)
            ),
            title = "FIA Testing System (POCT)",
            services = listOf<String>("cardiac markers . hormones . infection . autoimmune . tumor markers . diabetes HbA1c . 3 Other"),
            cardBottomOptions = CardBottomOptions.SeeResult,
            deviceCategory = DeviceCategory.FIATestingSystemPOCT
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_spirometer,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
                ConnectionState(ConnectionType.CABLE, ConnectionType.Status.DISCONNECTED)
            ),
            title = "Spirometer",
            services = listOf<String>("FVC . FEV1 . FEV1/FVC . FEF25/50/75 . FEF25–75"),
            deviceCategory = DeviceCategory.Spirometer,
            cardBottomOptions = CardBottomOptions.Empty

        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_hemoglobin_testing_system,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
                ConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED)
            ),
            title = "Hemoglobin Testing System",
            services = listOf<String>("Hb and Hct levels"),
            deviceCategory = DeviceCategory.HemoglobinTestingSystem,
            cardBottomOptions = CardBottomOptions.Empty

        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_pulse_oximeter,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
                ConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED),
            ),
            title = "Pulse Oximeter",
            services = listOf<String>("SpO2", "Pulse rate"),
            cardBottomOptions = CardBottomOptions.Pulse(PulseValues("98", "72")),
            deviceCategory = DeviceCategory.PulseOximeter
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_glucometer,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
            ),
            title = "Glucometer",
            cardBottomOptions = CardBottomOptions.Empty,
            deviceCategory = DeviceCategory.Glucometer,
            services = listOf("Blood Glucose")
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_thermometer,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.DISCONNECTED),
            ),
            title = "Thermometer",
            services = listOf<String>("SpO2", "Pulse rate"),
            cardBottomOptions = CardBottomOptions.Temperature,
            deviceCategory = DeviceCategory.Thermometer
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_urine_analyzer,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.USB, ConnectionType.Status.DISCONNECTED),
                ConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
            ),
            title = "Urine Analyzer",
            services = listOf<String>("GLU . BIL . SG . KET . BLD . PRO . URO . CR . UCA . NIT . LEU . VC . PH . MAL"),
            deviceCategory = DeviceCategory.UrineAnalyzer,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_doppler_ultrasound,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.WIFI, ConnectionType.Status.CONNECTED),
            ),
            title = "Doppler Ultrasound",
            services = listOf<String>("Length . Area . Angle . Obstetrics"),

            deviceCategory = DeviceCategory.DopplerUltrasound,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_ecg_workstation,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED),
            ),
            title = "ECG workstation",
            services = listOf<String>("12-lead ECG . 3-lead vector ECG"),
            deviceCategory = DeviceCategory.ECGWorkstation,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_digital_stethoscope,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED),
            ),
            title = "Digital Stethoscope",
            services = listOf<String>("Cardiopulmonary sounds (heart and lung sounds)"),
            deviceCategory = DeviceCategory.DigitalStethoscope,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_white_blood_cell_analyzer,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED),
                ConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
            ),
            title = "White Blood Cell AnalyzerCard",
            services = listOf<String>(" WBC count and differential: MON, NEU, EOS)"),
            deviceCategory = DeviceCategory.WhiteBloodCellAnalyzer,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_electronic_sphygmomanometer,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
            ),
            title = "White Blood Cell AnalyzerCard",
            services = listOf<String>("NIBP : adult, pediatric, neonatal"),
            deviceCategory = DeviceCategory.ElectronicSphygmomanometer,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_lipid_testing_system,
            connectionStateList = mutableStateListOf(
                ConnectionState(ConnectionType.WIFI, ConnectionType.Status.DISCONNECTED),
            ),
            title = "Lipid Testing System",
            services = listOf<String>("TC . TG . HDL . LDL . TC/HDL ratio"),
            deviceCategory = DeviceCategory.LipidTestingSystem,
            cardBottomOptions = CardBottomOptions.Empty
        ),//Lipid Testing System
    )
)

data class DeviceConfigure(
    val options: List<String> = listOf<String>(
        "All",
        "Vital Devices",
//        "Option 1",
//        "Option 2",
//        "Option 3"
    ), val deviceList: String = "Device List",
    val savedLocally: String = "Can't Sync,Saved Locally",
    val savedLocallyIcon: Int = R.drawable.ic_sync_locally,
    val addDevices: String = "Add Devices",
    val addDevicesIcon: Int = R.drawable.ic_add_device
)

data class PatentInfo(
    val age: String = "25",
    val name: String = "Ahmad",
    val gender: String = "Male",
    val insuranceCompany: String = "InsuranceCompany",
    val icon: Int = R.drawable.ic_collapse_arrow,
    val insuranceInfo: InsuranceInfo = InsuranceInfo(),
)

data class InsuranceInfo(
    val companyName: String = "CompanyName",
    val insuranceType: String = "Type",
    val number: String = "444444"
)

data class HomeScreenCard(
    val deviceIcon: Int,
    val connectionStateList: List<ConnectionState>,
    val title: String? = "",
    val deviceCategory: DeviceCategory,
    val services: List<String>? = null,
    val cardBottomOptions: CardBottomOptions,
)

enum class ConnectionType(
    val displayName: String,
    val connectedIcon: Int,
    val disconnectedIcon: Int
) {
    WIFI(
        displayName = "Wi-Fi",
        connectedIcon = R.drawable.ic_wifi_on,
        disconnectedIcon = R.drawable.ic_wifi_off
    ),
    BLUETOOTH(
        displayName = "Bluetooth",
        connectedIcon = R.drawable.ic_bluetooth_on,
        disconnectedIcon = R.drawable.ic_bluetooth_off
    ),
    CABLE(
        displayName = "CABLE",
        connectedIcon = R.drawable.ic_ethernet_cable_on,
        disconnectedIcon = R.drawable.ic_ethernet_cable_off
    ),
    USB(
        displayName = "USB",
        connectedIcon = R.drawable.ic_usb_on,
        disconnectedIcon = R.drawable.ic_usb_off
    );

    enum class Status { CONNECTED, DISCONNECTED }

    // 👇 helper to get correct icon based on status
    fun getIcon(status: Status): Int =
        if (status == Status.CONNECTED) connectedIcon else disconnectedIcon
}

data class ConnectionState(
    val type: ConnectionType,
    val status: ConnectionType.Status
)

//enum class CardBottomOptions(val info: String? = null, val pulseValues: PulseValues? = null) {
//    SeeResult("See Result"),
//    Empty("Empty"),
//    Temperature("36.5°C"),
//    Pulse("Pulse Info", PulseValues("98", "72"))
//}
sealed class CardBottomOptions(val title: String) {
    data object SeeResult : CardBottomOptions("See Result")
    data object Empty : CardBottomOptions("")
    data object Temperature : CardBottomOptions("36.5°C")
    data class Pulse(val pulseValues: PulseValues = PulseValues("98", "72")) :
        CardBottomOptions("Pulse Info")
}

data class PulseValues(val spo2: String = "33", val plus: String = "75")

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