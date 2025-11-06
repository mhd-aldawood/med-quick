package com.example.kotlintest.core.bluetooth

sealed class BluetoothCommand {
    data object SearchAndCommunicate : BluetoothCommand()
    data object StopBluetoothAndCommunication : BluetoothCommand()
}