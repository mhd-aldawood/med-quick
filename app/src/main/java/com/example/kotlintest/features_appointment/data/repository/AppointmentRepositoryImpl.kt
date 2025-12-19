package com.example.kotlintest.features_appointment.data.repository


import com.example.kotlintest.features_appointment.data.model.CreateAppointmentResponse
import com.example.kotlintest.features_appointment.data.model.DoctorsAvailabilityItemResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtiesResponse
import com.example.kotlintest.features_appointment.domain.model.CreateAppointmentReq
import com.example.kotlintest.features_appointment.domain.repository.AppointmentRepository
import com.example.kotlintest.util.data.model.MainResponse
import com.example.kotlintest.util.data.remote.MedLinkApi
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val api:MedLinkApi
): AppointmentRepository {

    override suspend fun getSpecialties(
        token: String,
        lang: String,
        pageNumber: Int
    ): MainResponse<SpecialtiesResponse> {
        return api.getSpecialties(token,lang, pageNumber)
    }

    override suspend fun getDoctorsSoonestAvailabilityList(
        token: String,
        lang: String,
        specialityId: Int,
        forecastedDaysCount: Int
    ): MainResponse<DoctorsAvailabilityItemResponse> {
        return api.getDoctorsSoonestAvailabilityList(token,lang, specialityId,forecastedDaysCount)
    }

    override suspend fun createAppointment(
        token: String,
        lang: String,
        req: CreateAppointmentReq
    ): MainResponse<CreateAppointmentResponse> {
        return api.bookAppointment(token,lang,req)
    }
}