package com.example.kotlintest.features_appointment.presentation.states

import com.example.kotlintest.features_appointment.data.model.CreateAppointmentResponse
import com.example.kotlintest.features_appointment.data.model.DoctorSlotsItemResponse
import com.example.kotlintest.features_appointment.data.model.DoctorsAvailabilityItemResponse
import com.example.kotlintest.features_appointment.data.model.ScheduleItemResponse
import com.example.kotlintest.features_appointment.data.model.SlotItemResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtiesResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtyItemResponse
import com.example.kotlintest.util.data.model.DateOfBirth
import com.example.kotlintest.util.data.model.RequestStates

data class AppointmentCreateScreenState(

    /////////////////////  PersonalInformationContent  States  /////////////////////
    val patientName:String = "",
    val patientDateOfBirth:DateOfBirth = DateOfBirth(),
    val patientGender:Int = 0,

    /////////////////////  AppointmentCreateComplaintContent States  /////////////////////
    val patientComplaint:String = "",
    val patientMedicalHistory:String = "",
    val patientNotes :String = "",

    /////////////////////  AppointmentCreateDetailsContent States  /////////////////////
    val specialtiesList : RequestStates<SpecialtiesResponse> =  RequestStates(data = SpecialtiesResponse()),
    val specialtySelectedItem : SpecialtyItemResponse = SpecialtyItemResponse(),
    val doctorsAvailabilityList: RequestStates<DoctorsAvailabilityItemResponse> =  RequestStates(data = DoctorsAvailabilityItemResponse()),
    val scheduleSelectedItem : ScheduleItemResponse = ScheduleItemResponse(),
    val doctorSelectedItem: DoctorSlotsItemResponse = DoctorSlotsItemResponse(),
    val slotSelectedItem : SlotItemResponse = SlotItemResponse(),

    /////////////////////  CreateAppointment States  /////////////////////

    val createAppointmentRequest: RequestStates<CreateAppointmentResponse> = RequestStates(data = CreateAppointmentResponse()),






    )
