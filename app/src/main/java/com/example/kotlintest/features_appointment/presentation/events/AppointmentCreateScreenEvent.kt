package com.example.kotlintest.features_appointment.presentation.events

import com.example.kotlintest.features_appointment.data.model.DoctorSlotsItemResponse
import com.example.kotlintest.features_appointment.data.model.ScheduleItemResponse
import com.example.kotlintest.features_appointment.data.model.SlotItemResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtyItemResponse
import com.example.kotlintest.util.data.model.DateOfBirth

sealed class AppointmentCreateScreenEvent {
    /////////////////////  PersonalInformationContent Events  /////////////////////
    class OnPatientNameChangedEvent(val patientName : String) : AppointmentCreateScreenEvent()
    class OnPatientDateOfBirthChangedEvent(val patientDateOfBirth : DateOfBirth) : AppointmentCreateScreenEvent()
    class OnPatientGenderChangedEvent(val patientGender : Int) : AppointmentCreateScreenEvent()

    /////////////////////  AppointmentCreateComplaintContent Events  /////////////////////
    class OnPatientComplaintChangedEvent(val patientComplaint : String) : AppointmentCreateScreenEvent()
    class OnPatientMedicalHistoryChangedEvent(val patientMedicalHistory : String) : AppointmentCreateScreenEvent()
    class OnPatientNotesChangedEvent(val patientNotes : String) : AppointmentCreateScreenEvent()

    /////////////////////  AppointmentCreateDetailsContent Events  /////////////////////
    class OnSelectedSpecialtyEvent(val specialty: SpecialtyItemResponse)  : AppointmentCreateScreenEvent()
    class OnSelectedScheduleEvent(val schedule: ScheduleItemResponse)  : AppointmentCreateScreenEvent()
    class OnSelectedDoctorEvent(val doctor: DoctorSlotsItemResponse)  : AppointmentCreateScreenEvent()
    class OnSelectedSlotEvent(val slot: SlotItemResponse)  : AppointmentCreateScreenEvent()

    /////////////////////  CreateAppointment Events  /////////////////////
    object OnCreateAppointmentEvent : AppointmentCreateScreenEvent()

}