package com.example.kotlintest.util.data.remote

import com.example.kotlintest.features_autentication.data.model.CheckOtpRequest
import com.example.kotlintest.features_autentication.data.model.ClinicItemResponse
import com.example.kotlintest.features_autentication.data.model.LoginResponse
import com.example.kotlintest.features_autentication.data.model.UserNameAvailabilityResponse
import com.example.kotlintest.features_autentication.domain.model.LoginReq
import com.example.kotlintest.features_autentication.domain.model.ResendOtpCodeReq
import com.example.kotlintest.features_autentication.domain.model.ResetPasswordReq
import com.example.kotlintest.features_appointment.data.model.CreateAppointmentResponse
import com.example.kotlintest.features_appointment.data.model.DoctorsAvailabilityItemResponse
import com.example.kotlintest.features_appointment.data.model.SpecialtiesResponse
import com.example.kotlintest.features_appointment.domain.model.CreateAppointmentReq
import com.example.kotlintest.util.data.model.MainResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface MedLinkApi {
    //Account
    @POST("v1/Accounts/Login")
    suspend fun login(@Body req: LoginReq): MainResponse<LoginResponse>

    @GET("v1/Clinics/GetLiteList")
    suspend fun getClinicsList(): MainResponse<ArrayList<ClinicItemResponse>>

    @GET("v1/Accounts/CheckUserName")
    suspend fun checkUserNameAvailable(@Query("userName")userName:String): MainResponse<UserNameAvailabilityResponse>

    @GET("v1/Accounts/CheckToken")
    suspend fun checkOtp(@Query("UserName") userName:String,@Query("Token") token:String): MainResponse<CheckOtpRequest>

    @POST("v1/Accounts/ResendCode")
    suspend fun resendCode(@Body req: ResendOtpCodeReq): MainResponse<Unit>

    @POST("v1/Accounts/ResetPasswordEmailConfirm")
    suspend fun resetPassword(@Body req: ResetPasswordReq): MainResponse<Unit>


    @GET("v1/indexes/Specialties/GetList")
    suspend fun getSpecialties(@Header("Authorization") token:String,@Header("culture") lang:String,@Query("PageNumber")pageNumber:Int, @Query("SortAsc")SortAsc: Boolean = true):MainResponse<SpecialtiesResponse>

    @GET("v1/Doctors/GetDoctorsSoonestAvailabilityList")
    suspend fun getDoctorsSoonestAvailabilityList(@Header("Authorization") token:String,@Header("culture") lang:String,@Query("specialityId")specialityId:Int, @Query("forcastedDaysCount")forcastedDaysCount:Int):MainResponse<DoctorsAvailabilityItemResponse>

    //Booking
    @POST("v1/Appointments/BookAppointment")
    suspend fun bookAppointment(@Header("Authorization") token:String,@Header("culture") lang:String,@Body req: CreateAppointmentReq):MainResponse<CreateAppointmentResponse>

}