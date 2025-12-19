package com.example.kotlintest.features_appointment.data.model

import com.example.kotlintest.util.data.model.PaginateModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpecialtiesResponse(
    @SerialName("items")
    var items:ArrayList<SpecialtyItemResponse> = ArrayList()
): PaginateModel()

