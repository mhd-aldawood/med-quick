package com.example.kotlintest.features_home.presentation.domin.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Patient (

    @SerialName("id"         ) var id         : Int,
    @SerialName("fullName"   ) var fullName   : String,
    @SerialName("gender"     ) var gender     : Int,
    @SerialName("ageInYears" ) var ageInYears : Int,
    @SerialName("imageToken" ) var imageToken : String

)