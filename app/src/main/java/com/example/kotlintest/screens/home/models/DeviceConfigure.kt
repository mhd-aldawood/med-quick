package com.example.kotlintest.screens.home.models

import com.example.kotlintest.R

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