package com.example.kotlintest.features_autentication.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserReqDto(
    @SerialName("id")
    var id:Int =0,

    @SerialName("userName")
    var userName:String="",

    @SerialName("email")
    var email:String="",

    @SerialName("phoneNumber")
    var phoneNumber:String="",

    @SerialName("firstName")
    var firstName:String="",

    @SerialName("lastName")
    var lastName:String="",

    @SerialName("type")
    var type:Int=0,

    @SerialName("gender")
    var gender:Int=0,

    @SerialName("dateOfBirth")
    var dateOfBirth:String="",

    @SerialName("active")
    var active:Boolean=false


)
