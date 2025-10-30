package com.example.kotlintest.core.model

sealed class DeviceUiEvent {
    data class Toast(val message: String) : DeviceUiEvent()
    data object FinishScreen : DeviceUiEvent() // if you need to navigate/finish
    data object UsbRemoved : DeviceUiEvent()
}