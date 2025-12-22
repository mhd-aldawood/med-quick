package com.example.kotlintest.util


import android.content.Context
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.util.DisplayMetrics
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale


//import us.zoom.sdk.*


object Const {
    const val App_Auth_State ="First"
    const val FIRST_TIME = "first_time"
    const val PHONE_NUMBER = "PHONE_NUMBER"
    const val PASSWORD = "PASSWORD"
    const val TOKEN = "token"
    const val SINGIN="SINGIN"
    const val COMPLETE = "complete"
    const val failedRefresh = "failedRefresh"
    const val FCM_TOKEN = "fcm_token"
    const val LANG = "lang"
    const val REFRESH_TOKEN = "refresh_token"
    const val EXPIRE_DATE = "expire_date"
    const val USER = "user"

    const val PROVIDER = "Provider"


    const val downloadURL= "https://dev-tabebyapi.azurewebsites.net/api/v1/Files/Download?token="
    const val url = "https://medlink-dev.azurewebsites.net/api/"
    const val refreshUrl = "https://dev-medlink-is4.azurewebsites.net/connect/"

    const val clientSecretTest ="26742906-4a77-409b-a7d8-ad3a3ef7858f"
    const val clientIdTest = "MedLink_platform_web_spa"
    const val CALL_TIMEOUT = 30000
    const val CONNECT_TIMEOUT = 30000
    const val READ_TIMEOUT = 30000
    const val WRITE_TIMEOUT = 30000

    const val START_ACTIVITY = 0
    const val startActivityWithFinish = 1
    const val startActivityWithClearBackStack = 2
    const val startActivityWithTop = 3


    fun convertArabicToWestern(str: String): String {
        val arabicToWestern = mapOf(
            '٠' to '0', '١' to '1', '٢' to '2', '٣' to '3', '٤' to '4',
            '٥' to '5', '٦' to '6', '٧' to '7', '٨' to '8', '٩' to '9','/' to '/'
        )
        return str.map { arabicToWestern[it] ?: it }.joinToString("")
    }


    fun convertJsonToMap(jsonString: String): Map<String, Any> {
        val gson = Gson()
        val type = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson<Map<String, Any>>(jsonString, type)
    }
    fun formatDouble(value: Double,digit:Int =2):Double {
        var decimalFormat =DecimalFormat("#.##",DecimalFormatSymbols(Locale.US))
        if (digit==3)
            decimalFormat = DecimalFormat("#.###",DecimalFormatSymbols(Locale.US))
        var amount = decimalFormat.format(value).toDouble()
        val decimalValue = convertArabicToWestern(amount.toString())
        return decimalValue.toDouble()
    }

    fun getAllValuesAsString(map: Map<String, Any>): Map<String, String> {
        val stringMap = mutableMapOf<String, String>()
        map.forEach { (key, value) ->
            stringMap[key] = valueToString(value)
        }
        return stringMap
    }

    fun getAllValuesConcatenated(map: Map<String, Any>): String {
        return map.values.joinToString(", ") {
            when (it) {
                is Map<*, *> -> getAllValuesConcatenated(it as Map<String, Any>)
                is Collection<*> -> it.joinToString(", ")
                else -> it.toString()
            }
        }
    }

    fun valueToString(value: Any): String {
        return when (value) {
            is Map<*, *> -> value.entries.joinToString(", ") { "${it.key}=${valueToString(it.value!!)}" }
            is List<*> -> value.joinToString(", ") { valueToString(it!!) }
            else -> value.toString()
        }
    }







    fun getDifferantWithUTC(): Int {
        // Get current local date and time
        val localDateTime = LocalDateTime.now()

        // Get current UTC offset in hours
        val localOffsetHours =
            ZoneOffset.systemDefault().rules.getOffset(localDateTime).totalSeconds / 3600

        // Get current UTC offset in hours
        val utcOffsetHours = ZoneOffset.UTC.rules.getOffset(localDateTime).totalSeconds / 3600
        val hourDifference = localOffsetHours - utcOffsetHours


        // Calculate the time difference in hour
        return hourDifference
    }

    fun CreateDate(year: Int, month: Int, day: Int, time: String): String {
        val localTime = LocalTime.parse(time)
        localTime.withSecond(0)
        // Extract hour, minute, and second components
        val hour = localTime.hour
        val min = localTime.minute
//        val sec = localTime.second
        val specificDateTime = LocalDateTime.of(year, month, day, hour, min)
        specificDateTime.withHour(hour)
        specificDateTime.withMinute(min)
        //   specificDateTime.withSecond(sec)
        val originalDateTime = LocalDateTime.parse(
            specificDateTime.toString(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        )
        // Format the LocalDateTime object into the desired format
        val formattedDateTimeString =
            originalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
        return formattedDateTimeString
    }

    fun getLangCode(lang: String):String{
        if(lang.equals("ar")){
            return "ar-AE"
        }else if (lang.equals("en")){
            return "en-GB"
        }
        return "en-GB"
    }

}

