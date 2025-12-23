package com.example.kotlintest.features_home.presentation.domin.model

import com.example.kotlintest.features_home.presentation.data.model.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCalendarReq(
    @SerialName("FromDate")
    val fromDate : String,
    @SerialName("ToDate")
    val toDate : String,
    @SerialName("OnlyMe")
    val onlyMe: Boolean?=null,
    @SerialName("SerialNumber")
    val serialNumber: String?=null,
    @SerialName("Status")
    val status: Status?=null,
    @SerialName("culture")
    val culture: String?=null,
)
