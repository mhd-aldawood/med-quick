package com.example.kotlintest.screens.home.models

import androidx.compose.runtime.mutableStateListOf
import com.example.kotlintest.R
import com.example.kotlintest.ui.theme.FrenchWine
import com.example.kotlintest.ui.theme.PrimaryMidLinkColor


data class DataHolder(
    val deviceConfigure: DeviceConfigure = DeviceConfigure(),
    val patentInfo: PatentInfo = PatentInfo(),
    val cardList: List<HomeScreenCard> = listOf(
        HomeScreenCard(
            deviceIcon = R.drawable.ic_fia_testing_system__poct,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.CABLE, ConnectionType.Status.CONNECTED),
                ModuleConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED)
            ),
            title = "FIA Testing System (POCT)",
            services = listOf<String>("cardiac markers . hormones . infection . autoimmune . tumor markers . diabetes HbA1c . 3 Other"),
            cardBottomOptions = CardBottomOptions.SeeResult(),
            deviceCategory = DeviceCategory.FIATestingSystemPOCT
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_spirometer,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
                ModuleConnectionState(ConnectionType.CABLE, ConnectionType.Status.DISCONNECTED)
            ),
            title = "Spirometer",
            services = listOf<String>("FVC . FEV1 . FEV1/FVC . FEF25/50/75 . FEF25–75"),
            deviceCategory = DeviceCategory.Spirometer,
            cardBottomOptions = CardBottomOptions.Empty

        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_hemoglobin_testing_system,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
                ModuleConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED)
            ),
            title = "Hemoglobin Testing System",
            services = listOf<String>("Hb and Hct levels"),
            deviceCategory = DeviceCategory.HemoglobinTestingSystem,
            cardBottomOptions = CardBottomOptions.Empty

        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_pulse_oximeter,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
                ModuleConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED),
            ),
            title = "Pulse Oximeter",
            services = listOf<String>("SpO2", "Pulse rate"),
            cardBottomOptions = CardBottomOptions.NewPulse(
                list = listOf(
                    PulseType(
                        text = "SpO2",
                        textColor = FrenchWine,
                        value = "95"
                    ), PulseType(text = "Pulse", textColor = PrimaryMidLinkColor, value = "75")
                )
            ),
            deviceCategory = DeviceCategory.PulseOximeter
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_glucometer,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
            ),
            title = "Glucometer",
            cardBottomOptions = CardBottomOptions.Empty,
            deviceCategory = DeviceCategory.Glucometer,
            services = listOf("Blood Glucose")
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_thermometer,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.DISCONNECTED),
            ),
            title = "Thermometer",
            services = listOf<String>("SpO2", "Pulse rate"),
            cardBottomOptions = CardBottomOptions.Temperature(),
            deviceCategory = DeviceCategory.Thermometer
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_urine_analyzer,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.USB, ConnectionType.Status.DISCONNECTED),
                ModuleConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
            ),
            title = "Urine Analyzer",
            services = listOf<String>("GLU . BIL . SG . KET . BLD . PRO . URO . CR . UCA . NIT . LEU . VC . PH . MAL"),
            deviceCategory = DeviceCategory.UrineAnalyzer,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_doppler_ultrasound,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.WIFI, ConnectionType.Status.CONNECTED),
            ),
            title = "Doppler Ultrasound",
            services = listOf<String>("Length . Area . Angle . Obstetrics"),

            deviceCategory = DeviceCategory.DopplerUltrasound,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_ecg_workstation,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED),
            ),
            title = "ECG workstation",
            services = listOf<String>("12-lead ECG . 3-lead vector ECG"),
            deviceCategory = DeviceCategory.ECGWorkstation,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_digital_stethoscope,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED),
            ),
            title = "Digital Stethoscope",
            services = listOf<String>("Cardiopulmonary sounds (heart and lung sounds)"),
            deviceCategory = DeviceCategory.DigitalStethoscope,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_white_blood_cell_analyzer,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.USB, ConnectionType.Status.CONNECTED),
                ModuleConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
            ),
            title = "White Blood Cell Analyzer",
            services = listOf<String>(" WBC count and differential: MON, NEU, EOS)"),
            deviceCategory = DeviceCategory.WhiteBloodCellAnalyzer,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_electronic_sphygmomanometer,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.BLUETOOTH, ConnectionType.Status.CONNECTED),
            ),
            title = "Electronic Sphygmomanometer",
            services = listOf<String>("NIBP : adult, pediatric, neonatal"),
            deviceCategory = DeviceCategory.ElectronicSphygmomanometer,
            cardBottomOptions = CardBottomOptions.Empty
        ),
        HomeScreenCard(
            deviceIcon = R.drawable.ic_lipid_testing_system,
            moduleConnectionStateList = mutableStateListOf(
                ModuleConnectionState(ConnectionType.WIFI, ConnectionType.Status.DISCONNECTED),
            ),
            title = "Lipid Testing System",
            services = listOf<String>("TC . TG . HDL . LDL . TC/HDL ratio"),
            deviceCategory = DeviceCategory.LipidTestingSystem,
            cardBottomOptions = CardBottomOptions.Empty
        ),//Lipid Testing System
    )
)
//enum class CardBottomOptions(val info: String? = null, val pulseValues: PulseValues? = null) {
//    SeeResult("See Result"),
//    Empty("Empty"),
//    Temperature("36.5°C"),
//    Pulse("Pulse Info", PulseValues("98", "72"))
//}

