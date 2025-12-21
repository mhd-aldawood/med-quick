package com.example.kotlintest.features_appointment.data.model

import com.example.kotlintest.features_appointment.domain.model.ComplaintInfoInBookDto
import com.example.kotlintest.features_appointment.domain.model.PatientInfoInBookDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CreateAppointmentResponse(
    @SerialName("id")
    var id:Int= 0,

    @SerialName("patientId")
    var patientId:Int= 0,

    @SerialName("patientInfo")
    var patientInfo:PatientInfoInBookDto= PatientInfoInBookDto(),

    @SerialName("clinicId")
    var clinicId:Int= 0,

    @SerialName("clinicName")
    var clinicName:String="",

    @SerialName("doctorId")
    var doctorId:Int= 0,

    @SerialName("doctorName")
    var doctorName:String="",

    @SerialName("nurseId")
    var nurseId:Int= 0,

    @SerialName("nurseName")
    var nurseName:String="",

    @SerialName("kitId")
    var kitId:Int= 0,

    @SerialName("kitSerialNumber")
    var kitSerialNumber:String="",

    @SerialName("createDate")
    var createDate:String="",

    @SerialName("scheduledDate")
    var scheduledDate:String="",

    @SerialName("addressId")
    var addressId:Int= 0,

    @SerialName("totalCharges")
    var totalCharges:Int= 0,

    @SerialName("currency")
    var currency:String="",

    @SerialName("isPaid")
    var isPaid: Boolean= false,

    @SerialName("status")
    var status:Int= 0,

    @SerialName("durationInMinutes")
    var durationInMinutes:Int= 0,

    @SerialName("paymentMethod")
    var paymentMethod:Int= 0,

    @SerialName("insuranceCompanyId")
    var insuranceCompanyId:Int= 0,

    @SerialName("insuranceTypeId")
    var insuranceTypeId:Int= 0,

    @SerialName("appointmentDetails")
    var appointmentDetails:ComplaintInfoInBookDto= ComplaintInfoInBookDto(),

    @SerialName("fileDetails")
    var fileDetails:ArrayList<String> = ArrayList(),

)

