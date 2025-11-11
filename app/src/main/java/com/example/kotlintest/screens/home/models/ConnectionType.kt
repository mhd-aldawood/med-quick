package com.example.kotlintest.screens.home.models

import com.example.kotlintest.R

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