package com.example.kotlintest.screens.home.models

import com.example.kotlintest.R

data class PatentInfo(
    val age: String = "25",
    val name: String = "Ahmad",
    val gender: String = "Male",
    val insuranceCompany: String = "InsuranceCompany",
    val icon: Int = R.drawable.ic_collapse_arrow,
    val insuranceInfo: InsuranceInfo = InsuranceInfo(),
)