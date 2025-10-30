package com.example.kotlintest.util


import android.util.Log
import com.example.kotlintest.BuildConfig

object Logger {
    var isEnabled: Boolean =  BuildConfig.DEBUG // true in debug, false in release

    fun d(tag:String,message: String) {
        if (isEnabled) Log.d(tag, message)
    }

    fun i(tag:String,message: String) {
        if (isEnabled) Log.i(tag, message)
    }

    fun w(tag:String,message: String) {
        if (isEnabled) Log.w(tag, message)
    }

    fun e(tag:String,message: String, throwable: Throwable? = null) {
        if (isEnabled) Log.e(tag, message, throwable)
    }
}
