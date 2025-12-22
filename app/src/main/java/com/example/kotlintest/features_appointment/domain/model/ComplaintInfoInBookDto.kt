package com.example.kotlintest.features_appointment.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ComplaintInfoInBookDto(
    @SerialName("patientComplaint")
    var patientComplaint:String="",

    @SerialName("patientMedicalHistory")
    var patientMedicalHistory:String="",

    @SerialName("notesForDoctor")
    var notesForDoctor:String="",

//    @SerialName("attachments")
//    var attachments:ArrayList<String> = ArrayList(),
)
