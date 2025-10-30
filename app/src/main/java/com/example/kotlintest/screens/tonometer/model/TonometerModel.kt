package com.example.kotlintest.screens.tonometer.model

@kotlinx.serialization.Serializable
data class TonometerModel(
    var Root: String? = null,
    var Info: ArrayList<Info> = arrayListOf(),
    var BloodPressureData: ArrayList<BloodPressureData> = arrayListOf()
)

@kotlinx.serialization.Serializable
data class BloodPressureData(

    var WorkingMode: String? = null,
    var MeasureMode: String? = null,
    var Date: String? = null,
    var Status: String? = null,
    var SystolicPressure: String? = null,
    var DiastolicPressure: String? = null,
    var AvgPressure: String? = null,
    var PulseRate: String? = null

)

@kotlinx.serialization.Serializable

data class Info(

    var TotalCount: Int? = null

)