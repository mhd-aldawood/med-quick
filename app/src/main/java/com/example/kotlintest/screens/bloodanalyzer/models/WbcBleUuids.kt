package com.example.kotlintest.screens.bloodanalyzer.models

import java.util.UUID

object WbcBleUuids {
    // Write data service
    val SERVICE_WRITE_UUID = UUID.fromString("0000FFE5-0000-1000-8000-00805F9B34FB")

    // Phone → Device (Write commands)
    val CHAR_WRITE_UUID = UUID.fromString("0000FFE9-0000-1000-8000-00805F9B34FB")

    // Notification service
    val SERVICE_NOTIFY_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB")

    // Device → Phone (Notifications with measurement data)
    val CHAR_NOTIFY_UUID = UUID.fromString("0000FFE4-0000-1000-8000-00805F9B34FB")

    // Standard CCCD for enabling notifications
    val CCCD_UUID = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
}
