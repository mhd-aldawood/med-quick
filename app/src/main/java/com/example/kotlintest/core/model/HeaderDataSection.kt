package com.example.kotlintest.core.model

import com.example.kotlintest.R
//TODO Make HeaderDataSection class for extension
data class HeaderDataSection(
    val title: String ,
    val titleIcon: Int,
    val cancelIcon: Int = R.drawable.ic_cancel,
    val cancelText: String="Cancel",
)
