package com.example.kotlintest.util

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun receiveFCMToken() {
    if (FinalValues.deviceToken.isNotEmpty()) {

        return
    }

    FirebaseMessaging.getInstance().token.addOnCompleteListener {
        if (!it.isSuccessful) {
            println("fcm token: getInstanceId failed\n ${it.exception}")
            return@addOnCompleteListener
        }

        // Get new Instance ID token
        val token =
            if (it.result == null) "" else it.result!!
        FinalValues.deviceToken = token

        // Log and toast
        Log.d("token","fcm token addOnCompleteListener: $token")
    }
}

fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
fun AppCompatActivity.setupFullScreenWithInsets(rootView: View) {
    // Allow content to draw behind system bars
    WindowCompat.setDecorFitsSystemWindows(window, false)

    // Apply insets to root view
    ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
        val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        // Check if navigation bar is visible
        val isNavigationBarVisible = insets.isVisible(WindowInsetsCompat.Type.navigationBars())

        // Update padding based on system bars visibility
        view.updatePadding(
            bottom = if (isNavigationBarVisible) systemBarsInsets.bottom else 0
        )

        // Return the insets so that they’re applied to child views if necessary
        insets
    }
}
fun List<String>.convertToString(): String {
    val stringBuilder = StringBuilder()
    for (i in  0..this.size-1 ){
        if(i != this.size - 1 ){
            stringBuilder.append(this.get(i)+",")
        }else
            stringBuilder.append(this.get(i))
    }
    return stringBuilder.toString()
}
fun AppCompatActivity.hideKeyBoard(){
        var view = this.getCurrentFocus();
        if (view != null) {
            var  imm = getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
}
fun AppCompatActivity.checkIfNavigationBar(rootView:View){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val navVisible = insets.isVisible(WindowInsets.Type.navigationBars())
            Log.d("navigation View", navVisible.toString())
            adjustLayoutForNavigationBar(navVisible)
            view.onApplyWindowInsets(insets)
        }

    }else {
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val isNavVisible = checkNavigationBarVisibility(rootView)
            Log.d("navigation View", isNavVisible.toString())
            adjustLayoutForNavigationBar(isNavVisible)
        }
    }
}
private fun adjustLayoutForNavigationBar(isVisible: Boolean) {
    if (isVisible) {
        // Adjust layout accordingly
    } else {
        // Full screen layout adjustments
    }
}
fun AppCompatActivity.isGestureNavigationEnabled() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {  // Android 10 (API 29) and above
//        try {
//            val mode: Int =
//                Settings.Secure.getInt(this.contentResolver, "secure_gesture_navigation")
//            return mode == 1 // 1 means gesture navigation is enabled, 0 means button navigation.
//        } catch (e: SettingNotFoundException) {
//            e.printStackTrace()
//        }
//    }
//    return false // Default to false if gesture setting is not found or below Android 10.
    val decorView = window.decorView
    decorView.setOnSystemUiVisibilityChangeListener { visibility ->
        if (visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0) {
            // The navigation bar is visible; perform any UI adjustments here
          Log.d("nvigation bar","visible")
        } else {
            // The navigation bar is hidden; perform any UI adjustments here if needed
            Log.d("nvigation bar","hiden")
        }
    }

}
private fun checkNavigationBarVisibility(view: View): Boolean {
    val heightDiff = view.rootView.height - view.height
    return heightDiff > 0 // When heightDiff is greater, navigation is visible
}
fun Context.startActivityTop(activity:Class<out Activity>,bundle:Bundle?= null){
    val intent = Intent(this,activity)
    if (bundle != null) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtras(bundle)
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
fun Context.hasLocationPermession():Boolean{
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )== PackageManager.PERMISSION_GRANTED
}
fun AppCompatActivity.isFragmentInBackStack(fragmentTagName: String): Boolean {
    val fragmentManager = this.supportFragmentManager
    for (entry in 0 until fragmentManager.backStackEntryCount) {
        if (fragmentTagName.equals(fragmentManager.getBackStackEntryAt(entry).name)) {
            return true
        }
    }
    return false
}
@SuppressLint("MissingPermission")
fun Context.getGPS(): DoubleArray {
    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    val providers = lm!!.getProviders(true)

    var l: Location? = null
    for (i in providers.indices.reversed()) {
        if(this.hasLocationPermession())
            l = lm.getLastKnownLocation(providers[i])
        if (l != null) break
    }
    val gps = DoubleArray(2)
    if (l != null) {
        gps[0] = l.getLatitude()
        gps[1] = l.getLongitude()
    }
    return gps
}
fun String.converToLocalTime():String{
    val parts = this.split(":")
    try {
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val uticTime = getUTCTimeInMillis(hours,minutes)
        val localCalendar = Calendar.getInstance()
        localCalendar.timeInMillis = uticTime
        val hour = localCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = localCalendar.get(Calendar.MINUTE)

        return "$hour:$minute"
    }catch (e:Exception){
        return ""
    }
}
fun String.convertUtcToLocal( outputFormat: String): String {
    // Parse the UTC date string
    val utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC)
    val dateTime = LocalDateTime.parse(this, utcFormatter)

    // Convert to local date time
    val localDateTime = dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

    // Format to the new date string with the desired local format
    val localFormatter = DateTimeFormatter.ofPattern(outputFormat)
    return localDateTime.format(localFormatter)
}
fun String.convertDateStringToMillis(): Long? {
   val localValue = this.convertUtcToLocalGeneral("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
    return try {
        val date = sdf.parse(localValue)
        date?.time
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
fun String.removeFractionalSeconds(): String {
    return this.replace(Regex("\\.(\\d+)$"), "")
}

fun String.convertUtcToLocalGeneral(outputFormat: String): String {
    // Parse the UTC date string
    val possiblePatterns = arrayOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd",
        "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    )

    for (pattern in possiblePatterns) {
        try {
            val utcFormatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.UTC)
            val dateTime = LocalDateTime.parse(this, utcFormatter)

            // Convert to local date time
            val localDateTime = dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

            // Format to the new date string with the desired local format
            val localFormatter = DateTimeFormatter.ofPattern(outputFormat)
            return localDateTime.format(localFormatter)
        } catch (e: Exception) {
            // Pattern doesn't match, continue to the next one
        }
    }
    return  ""
}
fun String.convertFormStringToLocalDateAndTime(format:String):LocalDateTime{
    val formatter = DateTimeFormatter.ofPattern(format)

    // Parse the string into a LocalDateTime object
    val localDateTime = LocalDateTime.parse(this, formatter)

   return localDateTime
}
fun String.getHourAndMinFromDate():String{

    // Parse the string into a LocalDateTime object
    val dateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    // Extract the hour and minute
    val hour = dateTime.hour
    val minute = dateTime.minute

    // Print the results
    println("Hour: $hour")
    println("Minute: $minute")
    return "${hour}:${minute}"
}
fun String.convertUtcToLocal( inputFormat:String,outputFormat: String): String {
    // Parse the UTC date string
    try {


        val utcFormatter = DateTimeFormatter.ofPattern(inputFormat).withZone(ZoneOffset.UTC)
        val dateTime = LocalDateTime.parse(this, utcFormatter)

        // Convert to local date time
        val localDateTime =
            dateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime()

        // Format to the new date string with the desired local format
        val localFormatter = DateTimeFormatter.ofPattern(outputFormat)
        return localDateTime.format(localFormatter)
    }catch (e:Exception){
        return ""
    }
}
fun String.converToAmPmFormat():String{
    val time = this.converToLocalTime()
    try {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val date = inputFormat.parse(time)
        return outputFormat.format(date)
    }catch (e:Exception){
        return ""
    }

}
fun getUTCTimeInMillis(hour: Int, minute: Int): Long {
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    utcCalendar.set(Calendar.HOUR_OF_DAY, hour)
    utcCalendar.set(Calendar.MINUTE, minute)
    utcCalendar.set(Calendar.SECOND, 0)
    utcCalendar.set(Calendar.MILLISECOND, 0)

    return utcCalendar.timeInMillis
}
fun String.displayDateBasedOnCurrent():String {
    val utcDateTime = ZonedDateTime.parse("${this}Z")
    // Convert UTC to local time zone
    val localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault())

    // Get the current date and time
    val nowLocalDateTime = LocalDateTime.now(ZoneId.systemDefault())

    when {
        localDateTime.toLocalDate().isEqual(nowLocalDateTime.toLocalDate()) -> {
            // If the date is today, calculate the hours difference
            try{
            val hoursDifference = Duration.between(localDateTime, nowLocalDateTime).toHours()
            val minutesDifference = Duration.between(localDateTime, nowLocalDateTime).toMinutes()
            when {
                hoursDifference > 0 -> return "$hoursDifference hours ago"
                minutesDifference > 0 -> return "$minutesDifference minutes ago"
                else -> return "Just now"
            }
        }catch (e:Exception){
            return ""
        }}
        localDateTime.toLocalDate().isEqual(nowLocalDateTime.minusDays(1).toLocalDate()) -> {
            // If the date is from yesterday
           return "Yesterday"
        }
        else -> {
            // Otherwise, display the date in a specific format
            return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
}
fun ImageView.moveViewToTarget(target: View) {

    val location1 = IntArray(2)
    val location2 = IntArray(2)

// Get the locations on the screen
    this.getLocationOnScreen(location1)
    target.getLocationOnScreen(location2)

// Calculate the x-offset
    val xOffset = location2[0] - location1[0]

// Animate imageView1 to move to the position of imageView2
    this.animate()
        .translationXBy(xOffset.toFloat())
        .setDuration(300) // Duration in milliseconds
        .start()
}
fun View.translateSelection(secondItem:View){
    val width = this.width
    val secondWidth = secondItem.width
    val targetWidth = width - secondWidth
    val widthAnimator = ValueAnimator.ofInt(this.width, targetWidth)
    if (secondItem.visibility != View.VISIBLE) {
        // Create a ValueAnimator for item2's translation animation
        widthAnimator.duration = 1000 // Duration in milliseconds
        widthAnimator.interpolator = AccelerateDecelerateInterpolator()
        widthAnimator.addUpdateListener { valueAnimator ->
            val width = valueAnimator.animatedValue as Int
            val layoutParams = this.layoutParams
            layoutParams.width = width
            layoutParams.height = this.height
            this.layoutParams = layoutParams
        }
        widthAnimator.start()
        // Set up a listener to make item1 visible after the width reduction animation completes
        widthAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                secondItem.visibility = View.VISIBLE

            }

        })
    }

}

fun AppCompatActivity.back(v:View) {
    v.setOnClickListener {
        onBackPressedDispatcher.onBackPressed()
    }
}
fun Uri.createFileFromUri(context: Context): File {
    val inputStream = context.contentResolver.openInputStream(this)
    val currentTime = LocalTime.now()
    val hour = currentTime.hour
    val minute = currentTime.minute
    val second = currentTime.second
    val timeInSeconds = hour * 3600 + minute * 60 + second
    val outputFile = File(context.cacheDir, timeInSeconds.toString()+"temp_file")
    val outputStream = FileOutputStream(outputFile)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return outputFile
}
fun Bitmap.bitmapToFile(context: Context) :File{
    val currentTime = LocalTime.now()
    val hour = currentTime.hour
    val minute = currentTime.minute
    val second = currentTime.second
    val timeInSeconds = hour * 3600 + minute * 60 + second
    val file = File(context.cacheDir, "image.jpg")
    try {
        val file = File(context.cacheDir, timeInSeconds.toString()+"image.jpg")
        val fos = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return file
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return file
}
fun String.convertToServerFormat():String{
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)  // Input format
    val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)  // Desired output format

    // Parse input string to Date object
    val dateObject: Date = inputFormat.parse(this)

    // Format Date object to desired output format
    return outputFormat.format(dateObject)

    return outputFormat.toString()
}
fun String.convertToViewFormat():String{
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val outputFormat = SimpleDateFormat("MM/dd/yyyy")


    // Parse input string to Date object
    try {
        val dateObject: Date = inputFormat.parse(this)

        // Format Date object to desired output format
        val formattedString: String = outputFormat.format(dateObject)

        return formattedString
    }catch (e:Exception){
        return ""
    }

}
fun File.getSizePerMB():Double{
    val sizeByte = this.length()
    val sizeInKB = sizeByte / 1024 // Convert to KB
    val sizeInMB:Double = (sizeInKB.toDouble() / 1024)
    return sizeInMB
}
fun File.getFileMimeType(): String? {
    val extension = getFileExtension()
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}
fun File.getFileExtension(): String {
    val lastDot = this.absolutePath.lastIndexOf(".")
    return if (lastDot == -1) {
        ""
    } else {
        this.absolutePath.substring(lastDot + 1)
    }
}

fun String.extractDate(): Triple<Int, Int, Int>? {
    try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val localDate = LocalDateTime.parse(this, formatter)

        val year = localDate.year
        val month = localDate.monthValue
        val day = localDate.dayOfMonth

        return Triple(year, month, day)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}
fun AppCompatActivity.updateLanguage(language: String,context:Context) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val resources: Resources = this.getResources()
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}
fun Context.startNewActivity(
    aClass: Class<*>,
    bundle: Bundle,
    status: Int = Const.START_ACTIVITY
) {
    Intent(this, aClass).also {
        it.putExtras(bundle)

        when (status) {
            Const.START_ACTIVITY -> {
                this.startActivity(it)
            }

            Const.startActivityWithFinish -> {
                this.startActivity(it)
                if (this is Activity)
                    this.finish()
            }
            Const.startActivityWithClearBackStack -> {
                it.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
                this.startActivity(it)
            }
            Const.startActivityWithTop -> {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                this.startActivity(it)
            }
        }
    }
}
fun YearMonth.getFirstAndLastDay():Pair<String, String> {
// Get first day of the month
    val firstDayOfMonth = this.atDay(1)

// Get last day of the month
    val lastDayOfMonth = this.atEndOfMonth()

// Convert to Date
    val firstDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant())
    val lastDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant())

// Format dates for display
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val firstString = formatter.format(firstDayOfMonth)
    val lastString = formatter.format(lastDayOfMonth)
    return Pair(firstString,lastString)
}
fun String.convertFromStringToLocalDate():LocalDate{
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val localDateTime = LocalDateTime.parse(this, formatter)

    // If you need just the date part without the time
    return localDateTime.toLocalDate()
}
fun String.convertToDayMonthNameFormat():Pair<String, String>{
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",  Locale.getDefault())
    var outputFormat = SimpleDateFormat("E, dd MMM yyyy",  Locale.getDefault())
    var outputTime = SimpleDateFormat("hh:mm a", Locale.getDefault())

    try {
        val date = inputFormat.parse(this.convertUtcToLocal("yyyy-MM-dd'T'HH:mm:ss"))
        val formattedDate = outputFormat.format(date)
        val time = outputTime.format(date)
        return Pair(formattedDate,time) // Output: Wed, 01 Mar 2024
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return Pair("","")
}
fun Context.createNotificationChanel(chanelId:String){
    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val importance = NotificationManager.IMPORTANCE_HIGH
    val notificationChannel = NotificationChannel(chanelId, "medquick", importance)

    notificationManger.createNotificationChannel(notificationChannel)
}


fun Context.fragmentResume(manger:FragmentManager,fragmentId:Int){
    manger.addOnBackStackChangedListener {
        val fragment: Fragment? =
            manger.findFragmentById(fragmentId)
        if (fragment != null) {
            fragment.onResume()
        }
    }
}
// Extension function to compare multiple attribute values of two objects
fun <T, R> compareAttributes(obj1: T, obj2: T, selectors: List<(T) -> R>): Boolean {
    for (selector in selectors) {
        if (selector(obj1) != selector(obj2)) {
            return false
        }
    }
    return true
}
fun View.Scaling(){
    // Create scale animations for X and Y axes
    val scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.2f, 1f)
    val scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.2f, 1f)

    // Combine animations
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(scaleX, scaleY)
    animatorSet.duration = 300 // Animation duration in milliseconds
    animatorSet.start()
}

fun File.getMp3DurationFromFile(): String {
    val retriever = MediaMetadataRetriever()
    return try {
        if (this.exists()) {
            retriever.setDataSource(this.absolutePath) // Set the file path
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val durationMs = durationStr?.toLongOrNull() ?: 0L

            // Convert milliseconds to MM:SS format
            val minutes = durationMs / 1000 / 60
            val seconds = (durationMs / 1000) % 60

            return String.format("%02d:%02d", minutes, seconds) // Format duration as MM:SS
        } else {
            return ""
        }
    } catch (e: Exception) {
        return ""
    } finally {
        retriever.release() // Release resources
    }
}