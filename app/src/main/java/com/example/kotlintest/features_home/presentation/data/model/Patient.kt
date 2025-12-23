package com.example.kotlintest.features_home.presentation.data.model

import com.example.kotlintest.core.model.Gender
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Patient(

    @SerialName("id") var id: Int,
    @SerialName("fullName") var fullName: String,
    @SerialName("gender") var gender: Int,
    @SerialName("ageInYears") var ageInYears: Int,
    @SerialName("imageToken") var imageToken: String,
    @SerialName("dateOfBirth") var dateOfBirth: String,
) {
    fun getGender(): Gender {
        return when (gender) {
            1 -> {
                Gender.Male
            }

            2 -> {
                Gender.Female
            }

            else -> {
                Gender.Female
            }
        }
    }
}
