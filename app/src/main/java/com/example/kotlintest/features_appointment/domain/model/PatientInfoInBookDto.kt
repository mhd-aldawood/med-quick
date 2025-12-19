package com.example.kotlintest.features_appointment.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientInfoInBookDto(
//    @SerialName("uuid")
//    var uuid:String="",

    @SerialName("name")
    var name:String="",

    @SerialName("dateOfBirth")
    var dateOfBirth:String="",

    @SerialName("gender")
    var gender:Int=1,

//    @SerialName("idFrontImageToken")
//    var idFrontImageToken:String="",
//
//    @SerialName("idBackImageToken")
//    var idBackImageToken:String="",
//
//    @SerialName("passportFrontImageToken")
//    var passportFrontImageToken:String="",
//
//    @SerialName("passportBackImageToken")
//    var passportBackImageToken:String="",

)
