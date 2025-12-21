package com.example.kotlintest.util.data.model

sealed class MainResources<T>(val data:T?=null,
                              val isSuccess:Boolean?=false,
                              val hasException:Boolean?=false,
                              val fullExceptionMessage:String?=null,
                              val hasCustomStatusCode:Boolean?=false,
                              val status:Int?=0,
                              val message:String?="",) {
    class isLoading<T>(data: T?=null):MainResources<T>(data = data)
    class isError<T>(data: T?=null,message: String):MainResources<T>(data,message=message)
    class Sucess<T>(data: T?):MainResources<T>(data)
    class isUnAuthorized<T>(message: String):MainResources<T>(message=message)
}