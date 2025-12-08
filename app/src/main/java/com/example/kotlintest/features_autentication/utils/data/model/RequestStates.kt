package com.example.kotlintest.features_autentication.utils.data.model
open class RequestStates<T>(
    var isLoading:Boolean = false,
    var isError:Boolean = false,
    var isSuccess:Boolean = false,
    var isSubmit:Boolean = false,
    var error:String="",
    var data:T
)