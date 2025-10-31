package com.example.kotlintest.core.model

import serial.jni.BluConnectionStateListener
import serial.jni.DataUtils

interface DataUtilsFactory {
    fun create(
        address: String,
        lead: Int = DataUtils.ECG_LEAD_WILSON,
        debug: Boolean = false,
        listener: BluConnectionStateListener
    ): DataUtils
}