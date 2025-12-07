package com.example.kotlintest.screens.tonometer.models

enum class ChosenLamp(val selectStatus: SelectStatus) {
    LeftArm(selectStatus = SelectStatus.NotSelected),
    RightArm(selectStatus = SelectStatus.NotSelected),
    LeftLeg(selectStatus = SelectStatus.Selected),
    RightLeg(selectStatus = SelectStatus.NotSelected)
}












