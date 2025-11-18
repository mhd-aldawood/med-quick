package com.example.kotlintest.screens.poct.models

data class DeviceResult(
    val patientId: String?,
    val patientName: String?,
    val orderNumber: String?,
    val testCode: String?,
    val testName: String?,
    val resultValue: String?,
    val resultUnit: String?,
    val referenceRange: String?,
    val interpretation: String?,
    val resultStatus: String?,
    val resultDateTime: String?,
    val lotNumber: String?,
    val serialNumber: String?,
    val specimenType: String?
)
