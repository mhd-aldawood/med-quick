package com.example.kotlintest.data.source.network

@kotlinx.serialization.Serializable
data class User(
    var Id: String? = null,
    var FirstName: String? = null,
    var LastName: String? = null,
    var DateOfBirth: String? = null,
    var Address: Address? = Address()

)

@kotlinx.serialization.Serializable
data class Address(
    var HouseNumber: String? = null,
    var Street: String? = null,
    var State: String? = null,
    var ZipCode: String? = null,
    var Country: String? = null

)