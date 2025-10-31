package com.example.kotlintest.core.model

sealed class ConnectionState {
    data object Idle : ConnectionState()
    data object Connecting : ConnectionState()
    data object Connected : ConnectionState()
    data object Disconnecting : ConnectionState()
    data object Failed : ConnectionState()
    data object Interrupted : ConnectionState()
}