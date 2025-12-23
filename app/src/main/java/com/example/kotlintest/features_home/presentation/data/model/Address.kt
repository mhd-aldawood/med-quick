package com.example.kotlintest.features_home.presentation.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address (

    @SerialName("id"             ) var id             : Int,
    @SerialName("city"           ) var city           : String,
    @SerialName("country"        ) var country        : String,
    @SerialName("streetName"     ) var streetName     : String,
    @SerialName("buildingNumber" ) var buildingNumber : String,
    @SerialName("postCode"       ) var postCode       : String,
    @SerialName("flatNumber"     ) var flatNumber     : String,
    @SerialName("longitude"      ) var longitude      : Int,
    @SerialName("latitude"       ) var latitude       : Int

){
    fun getFullLocation():String = "$country  $city $streetName $buildingNumber $flatNumber"
}

