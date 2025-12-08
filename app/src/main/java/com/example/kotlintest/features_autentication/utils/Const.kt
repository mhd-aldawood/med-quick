package com.example.kotlintest.features_autentication.utils


import android.content.Context
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.util.DisplayMetrics
import android.view.View
import com.example.kotlintest.features_autentication.utils.data.model.AppAuthState
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

    const val OTP = "otp"
    const val Specility = "Specility"
    const val CinicId = "CinicId"
    const val PHARMACY = "pharmacy"
    const val LAB = "lab"
    const val DOCTOR="doctor"
    const val AppointementTypeBundle = "AppointementType"
    const val EfawaterComBundle = "EfawaterComBundle"
    const val ID = "id"
    const val AMOUNT = "amount"
    const val COUNTRYID="country_id"
    const val TYPE = "TYPE"
    const val NAME = "NAME"
    const val ITEM ="item"
    const val LAT = "lat"
    const val LON = "lon"
    const val NOTIFICATIONTYPE = "notificationType"
    const val REBOOK="rebook"
    const val CURRENCY = "currency"
    const val PATIENT_ID ="patient_id"
    const val PATIENT_F_NAME = "patient_f_name"
    const val MODULE_TYPE = "module_type"
    const val APPOINTEMENT_ID = "appointement_id"
    const val FROM_GENERAL = "from_general"
    const val FROM_URGENT = "from_urgent"
    const val FROM_CHANGE_PHARMACY ="FROM_CHANGE_PHARMACY"
    const val FROM_CHANGE_LAB ="FROM_CHANGE_LAB"
    const val FROM_REORDER = "FROM_REORDER"
    const val FROM_BOOKING = "FROM_BOOKING"
    const val MEDICATION = "MEDICATION"
    const val RESCHUDLE = "RESCHUDLE"
    const val URL = "url"
    const val  FROM_UAE_PASS = "from_uae_pass"
    const val BASE_URL_UAE_PASS = "https://id.uaepass.ae/idshub/"
    const val RECTOKEN = "6LcsG24qAAAAAK0-RIa8th09509Han3pkCbyAxh1"

    //const
//    const val url = "https://prod-medquick-api.azurewebsites.net/api/"
//    const val refreshUrl = "https://prod-medquick-is4.azurewebsites.net/connect/"
//    const val downloadURL = "https://prod-medquick-api.azurewebsites.net/api/v1/Files/Download?token="
    const val downloadURL= "https://dev-tabebyapi.azurewebsites.net/api/v1/Files/Download?token="
    const val url = "https://medlink-dev.azurewebsites.net/api/"
    const val refreshUrl = "https://dev-medlink-is4.azurewebsites.net/connect/"
    const val DirectionApiUrl="https://maps.googleapis.com/maps/"
//    const val clientIdTest = "med_quick_mobile_app"
//    const val clientSecretTest = "65035458-9f6d-b190-5b4c-f44afc7620d6"
    const val clientSecretTest ="ea5bfed0-7e3d-4d4c-8a9b-c840fb21806d"
    const val clientIdTest = "api_ids4_swaggerui"
    const val CALL_TIMEOUT = 30000
    const val CONNECT_TIMEOUT = 30000
    const val READ_TIMEOUT = 30000
    const val WRITE_TIMEOUT = 30000

    const val START_ACTIVITY = 0
    const val startActivityWithFinish = 1
    const val startActivityWithClearBackStack = 2
    const val startActivityWithTop = 3

    object DirectPay{

        const val Payment_Type = "1" //1:for PostPaid 2: for Prepaid”

//        const val ENV = "prod";// for production
//        const val Biller_Code = "707" // for production
//        const val Service_Code = "32943" // for production
//        const val Sdk_Key = "b5f86cfb531a4ce385a26b2da6eb6548" // for production

        const val ENV = "";
        const val Biller_Code = "1786"
        const val Service_Code = "113263"
        const val Sdk_Key = "bacfd84a09df469b97868f04c6b21972"
    }



    fun convertArabicToWestern(str: String): String {
        val arabicToWestern = mapOf(
            '٠' to '0', '١' to '1', '٢' to '2', '٣' to '3', '٤' to '4',
            '٥' to '5', '٦' to '6', '٧' to '7', '٨' to '8', '٩' to '9','/' to '/'
        )
        return str.map { arabicToWestern[it] ?: it }.joinToString("")
    }
    const val DoctorSection = "AndroidDisplayDoctorSection"
    const val FollowUpSection = "AndroidDisplayFollowUpSection"
    const val ClinicSection = "AndroidDisplayClinicSection"
    const val SpecilitySection = "AndroidDisplaySpecialitiesSection"

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

    fun setWidthPercentForElement(view: View, percent: Float) {
        val params = view.layoutParams
        val screenWidth = getScreenWidth(view.context)
        params.width = (screenWidth * percent).toInt() // Set the width to 70% of the screen width
        view.setLayoutParams(params)
        view.layoutParams = params
    }

    fun getScreenWidth(context: Context): Int {
        val resources: Resources = context.resources
        val displayMetrics: DisplayMetrics = resources.getDisplayMetrics()
        return displayMetrics.widthPixels
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












    fun getLocationDetails(context: Context, latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                address = addresses[0]
                addressText = address.getAddressLine(0) ?: "Address not available"
            } else {
                return null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return address
    }




}

