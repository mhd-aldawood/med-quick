package com.example.kotlintest.screens.home.models

data class HomeScreenCard(
    val deviceIcon: Int,
    val moduleConnectionStateList: List<ModuleConnectionState>,
    val title: String,
    val deviceCategory: DeviceCategory,
    val services: List<String>? = null,
    val cardBottomOptions: CardBottomOptions,
)