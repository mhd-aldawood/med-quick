package com.example.kotlintest.screens.tonometer

import androidx.compose.ui.graphics.Color
import com.example.kotlintest.R
import kotlinx.coroutines.selects.select

enum class ChosenLamp(val selectStatus: SelectStatus) {
    LeftArm(selectStatus = SelectStatus.NotSelected),
    RightArm(selectStatus = SelectStatus.NotSelected),
    LeftLeg(selectStatus = SelectStatus.Selected),
    RightLeg(selectStatus = SelectStatus.NotSelected)
}

enum class PositionType(val text: String) {
    Sitting("Sitting"),
    Lying("Lying")
}

data class PatientPosition(
    val type: PositionType,
    val selectStatus: SelectStatus,
    val icon: Int
)

enum class SelectStatus {
    Selected, NotSelected
}

enum class AgeGroup(val text: String) {
    Adult(text = "Adult"),
    NewPorn(text = "NewPorn"),
    Child(text = "Child"),
    None(text = "None")
}

data class AgeGroupBtn(
    val ageGroup: AgeGroup,
    val ageGroupTextColor: Color,
    val backgroundColor: Color
)

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

