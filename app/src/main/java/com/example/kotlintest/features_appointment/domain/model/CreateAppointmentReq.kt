package com.example.kotlintest.features_appointment.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CreateAppointmentReq(
    @SerialName("patient")
    var patient:PatientInfoInBookDto= PatientInfoInBookDto(),

    @SerialName("appointmentDetails")
    var appointmentDetails:ComplaintInfoInBookDto= ComplaintInfoInBookDto(),

    @SerialName("scheduleDateAndTime")
    var scheduleDateAndTime:String="",

    @SerialName("doctorId")
    var doctorId:Int= 0,

//    @SerialName("paid")
//    var paid: Boolean= false,
//
//    @SerialName("paymentMethod")
//    var paymentMethod:Int= 0,
//
//    @SerialName("insuranceCompanyId")
//    var insuranceCompanyId:Int= 0,
//
//    @SerialName("insuranceType")
//    var insuranceType:Int= 0,
//
//    @SerialName("kitId")
//    var kitId:Int= 0,

    )
