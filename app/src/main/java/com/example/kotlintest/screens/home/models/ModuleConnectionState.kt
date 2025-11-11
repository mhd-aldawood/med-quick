package com.example.kotlintest.screens.home.models

data class ModuleConnectionState(
    val type: ConnectionType,
    val status: ConnectionType.Status
)