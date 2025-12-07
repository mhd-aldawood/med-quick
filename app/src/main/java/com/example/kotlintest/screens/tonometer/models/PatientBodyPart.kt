package com.example.kotlintest.screens.tonometer.models

sealed class PatientBodyPart(
    open val text: String,
    open val fontSize: Int
) {
    data class LeftArm(
        override val text: String="Left Arm",
        override val fontSize: Int = 14
    ) : PatientBodyPart(text,fontSize)

    data class LeftLeg(
        override val text: String="Left Leg",
        override val fontSize: Int = 14
    ) : PatientBodyPart(text,fontSize)

    data class RightArm(
        override val text: String="Right Arm",
        override val fontSize: Int = 14
    ) : PatientBodyPart(text,fontSize)

    data class RightLeg(
        override val text: String="Right Leg",
        override val fontSize: Int = 14
    ) : PatientBodyPart(text,fontSize)
}