package com.example.kotlintest.core.model

sealed class ConnectionState {
    data object Idle : ConnectionState()
    data object Connecting : ConnectionState()
    data object Connected : ConnectionState()
    data object Disconnecting : ConnectionState()
    data object Failed : ConnectionState()
    data object Interrupted : ConnectionState()
}
/*
*
     viewModelScope.launch {
        deviceConfigure
            .map { it.connectionState }   // Only listen to connectionState
            .distinctUntilChanged()       // Ignore duplicates
            .collect { state ->
                when (state) {
                    ConnectionState.Idle -> { /* do something */ }
                    ConnectionState.Connecting -> { /* show loading */ }
                    ConnectionState.Connected -> { /* device is ready */ }
                    ConnectionState.Disconnecting -> { /* handle */ }
                    ConnectionState.Failed -> { /* show error */ }
                    ConnectionState.Interrupted -> { /* handle interruption */ }
                }
            }
    }
*/


