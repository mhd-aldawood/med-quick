package com.example.kotlintest.features_appointment.domain.repository

import com.example.kotlintest.features_appointment.data.model.CreateAppointmentResponse
import com.example.kotlintest.features_appointment.data.model.DoctorsAvailabilityItemResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtiesResponse
import com.example.kotlintest.features_appointment.domain.model.CreateAppointmentReq
import com.example.kotlintest.util.data.model.MainResponse

interface AppointmentRepository {
    suspend fun getSpecialties(token: String,lang:String,pageNumber:Int): MainResponse<SpecialtiesResponse>

    suspend fun getDoctorsSoonestAvailabilityList(token: String,lang:String,specialityId:Int , forecastedDaysCount:Int): MainResponse<DoctorsAvailabilityItemResponse>

    suspend fun createAppointment(token: String,lang:String,req: CreateAppointmentReq):MainResponse<CreateAppointmentResponse>
}