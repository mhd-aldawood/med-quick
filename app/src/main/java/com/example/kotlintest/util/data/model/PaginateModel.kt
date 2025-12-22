package com.example.kotlintest.util.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class PaginateModel(
    @SerialName("itemsCount")
    var itemsCount:Int=0,
    @SerialName("pageNumber")
    var pageNumber:Int=0,
    @SerialName("pageSize")
    var pageSize:Int=0,
    @SerialName("pagesCount")
    var pagesCount:Int=0,
)