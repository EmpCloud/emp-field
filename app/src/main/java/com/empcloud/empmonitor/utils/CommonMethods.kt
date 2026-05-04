package com.empcloud.empmonitor.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.view.Window
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.empcloud.empmonitor.R
import com.empcloud.empmonitor.data.remote.request.send_location.LocationList
import com.empcloud.empmonitor.ui.activity.SplashActivity
import com.empcloud.empmonitor.utils.broadcast_services.AutoCheckoutReceiver
import com.empcloud.empmonitor.utils.broadcast_services.StopocationService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt


object CommonMethods {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatApiTime(apiTime: String): String {
        // Define the formatter for the incoming time string
//        val inputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
//        // Parse the time string to a LocalTime object
//        val time = LocalTime.parse(apiTime, inputFormatter)
//        // Define the formatter for the output time string with uppercase AM/PM
//        val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a")
//        // Format the LocalTime object to the desired pattern
//        val formattedTime = time.format(outputFormatter)
//        // Convert the AM/PM part to lowercase
//        return formattedTime.replace("am", "AM").replace("pm", "PM")

        val outputFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        // Regular expression patterns for matching "HH:mm:ss" and "HH:mm"
        val patternWithSeconds = Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$")
        val patternWithoutSeconds = Pattern.compile("^\\d{2}:\\d{2}$")

        return try {
            // Check and parse the input time string
            val time = when {
                patternWithSeconds.matcher(apiTime).matches() -> {
                    val inputFormatterWithSeconds = DateTimeFormatter.ofPattern("HH:mm:ss")
                    LocalTime.parse(apiTime, inputFormatterWithSeconds)
                }

                patternWithoutSeconds.matcher(apiTime).matches() -> {
                    val inputFormatterWithoutSeconds = DateTimeFormatter.ofPattern("HH:mm")
                    LocalTime.parse(apiTime, inputFormatterWithoutSeconds)
                }

                else -> throw DateTimeParseException("Invalid time format", apiTime, 0)
            }

            // Format the LocalTime object to the desired pattern and convert the AM/PM part to uppercase
            val formattedTime = time.format(outputFormatter)
            formattedTime.replace("am", "AM").replace("pm", "PM")
        } catch (e: DateTimeParseException) {
            "Invalid time format"
        }
    }


    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    fun switchFragment(activity: FragmentActivity, fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun getSharedPrefernce(activity: FragmentActivity, prefName: String): String {

        val data = activity.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
        val getValue = data.getString(prefName, "")
        return getValue!!
    }

    fun getSharedPrefernceBoolean(activity: FragmentActivity, prefName: String): Boolean {

        val data = activity.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
        val getValue = data.getBoolean(prefName, false)
        return getValue!!
    }

    fun getSharedPrefernceInteger(activity: FragmentActivity, prefName: String): Int {

        val data = activity.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
        val getValue = data.getInt(prefName, 0)
        return getValue!!
    }

    fun saveSharedPrefernce(
        activity: FragmentActivity,
        prefName: String,
        dataPref: String,
        dataValue: String
    ) {

        val data = activity.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
        data.edit().putString(dataPref, dataValue).apply()

    }

    fun saveSharedPrefernceBoolean(
        activity: FragmentActivity,
        prefName: String,
        dataPref: String,
        dataValue: Boolean
    ) {

        val data = activity.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
        data.edit().putBoolean(dataPref, dataValue).apply()

    }
    fun saveSharedPrefernceInteger(
        activity: FragmentActivity,
        prefName: String,
        dataPref: String,
        dataValue: Int
    ) {

        val data = activity.getSharedPreferences(prefName, AppCompatActivity.MODE_PRIVATE)
        data.edit().putInt(dataPref, dataValue).apply()

    }


    fun formatTime(time: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = time
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
        return String.format(
            Locale.getDefault(),
            "%02d:%02d %s",
            hourOfDay % 12,
            minute,
            amPm.toUpperCase(Locale.getDefault())
        )
    }

    fun getLocationDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
        val R = 6371 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distanceKm = R * c // Distance in kilometers

        return if (distanceKm >= 1) {
            "${distanceKm.roundToInt()} km"
        } else {
            val distanceMeters = (distanceKm * 1000).roundToInt()
            "$distanceMeters m"
        }
    }



    fun getLocationDistanceMeter(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val R = 6371000 // Radius of the Earth in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distanceMeters = (R * c).roundToInt() // Distance in meters

        return distanceMeters
    }


    fun combineDateAndTime(time: String): String {
        try {
            // Get current date
            val currentDate = Calendar.getInstance().time

            // Format current date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedCurrentDate = dateFormat.format(currentDate)

            // Parse the provided time string into a Date object
            val timeFormat = SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            ) // Pattern for 12-hour format with AM/PM marker
            val timeDate = timeFormat.parse(time)

            // Combine the current date with the parsed time
            val combinedDate = Calendar.getInstance()
            combinedDate.time = dateFormat.parse(formattedCurrentDate) // Set the current date part
            combinedDate.set(Calendar.HOUR_OF_DAY, timeDate.hours)
            combinedDate.set(Calendar.MINUTE, timeDate.minutes)
            combinedDate.set(Calendar.SECOND, 0) // Assuming seconds should be set to zero

            // Format the combined date and time
            return SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(combinedDate.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            return "" // Return empty string or handle the error as appropriate
        }
    }


    fun getCurrentDate(): String {
        // Create a SimpleDateFormat object with the desired format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Get the current date
        val currentDate = Date()

        // Format the current date
        return dateFormat.format(currentDate)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getStartDateOfCurrentMonth(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()
        return currentDate.withDayOfMonth(1).format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEndDateOfCurrentMonth(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = LocalDate.now()
        return currentDate.withDayOfMonth(currentDate.lengthOfMonth()).format(formatter)
    }


    fun getFileFromUriGallery(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val tempFile = File(context.cacheDir, fileName)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

//    fun getFileFromUriGallery(context: Context, uri: Uri): String? {
//        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
//        cursor?.use {
//            if (it.moveToFirst()) {
//                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                return it.getString(columnIndex)
//            }
//        }
//        return null
//    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val contentResolver = context.contentResolver

        // Handle different Uri schemes
        return when (uri.scheme) {
            "content" -> {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        return File(cursor.getString(columnIndex))
                    }
                }
                null
            }

            "file" -> {
                return File(uri.path)
            }

            else -> {
                null // Unsupported scheme
            }
        }
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun getBitmapFromSharedPreferences(context: Context, key: String): Bitmap? {
        val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val base64String = sharedPreferences.getString(key, null)
        return if (base64String != null) {
            base64ToBitmap(base64String)
        } else {
            null
        }
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun saveBitmapToSharedPreferences(context: Context, bitmap: Bitmap, key: String) {
        val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val base64String = bitmapToBase64(bitmap)
        editor.putString(key, base64String)
        editor.apply()
    }

    fun bitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun stringToBitmap(encodedString: String): Bitmap {
        val decodedBytes: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun getCurrentMonthAndDate(): Pair<String, String> {
        val calendar = Calendar.getInstance()

        // Get the current month abbreviation (e.g., "OCT")
        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val monthAbbreviation = monthFormat.format(calendar.time)

        // Get the current date as a two-digit number (e.g., "10")
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val twoDigitDate = dateFormat.format(calendar.time)

        return Pair(monthAbbreviation, twoDigitDate)
    }

    fun extractMonthAndDay(
        originalDateString: String,
        originalFormat: String
    ): Pair<String, String> {
        val originalDateFormat = SimpleDateFormat(originalFormat, Locale.getDefault())

        return try {
            val originalDate = originalDateFormat.parse(originalDateString)
            val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
            val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

            val month = monthFormat.format(originalDate)
            val day = dayFormat.format(originalDate)

            Pair(month, day)
        } catch (e: Exception) {
            // Handle parsing errors
            Pair("Invalid Month", "Invalid Day")
        }
    }

    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun calculateTimeWorked(loginTime: String, logoutTime: String): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val loginDate = dateFormat.parse(loginTime)
        val logoutDate = dateFormat.parse(logoutTime)

        val differenceInMillis = logoutDate.time - loginDate.time

        val hours = TimeUnit.MILLISECONDS.toHours(differenceInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(differenceInMillis) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun clearStringFromSharedPreferences(context: Context, key: String) {
        val prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()
    }


    fun isValidDateFormat(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false

        return try {
            val date = dateFormat.parse(dateString)
            date != null
        } catch (e: Exception) {
            false
        }
    }

    fun isValidDateFormatChange(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        return try {
            val date = dateFormat.parse(dateString)
            date != null
        } catch (e: Exception) {
            false
        }
    }

    fun isValidTimeFormat(timeString: String): Boolean {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        timeFormat.isLenient = false

        return try {
            val time = timeFormat.parse(timeString)
            time != null
        } catch (e: Exception) {
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isDateBeforeCurrent(dateStr: String): Boolean {
        // Define the date format (adjust as necessary to match your input format)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Parse the input date string to a LocalDate
        val givenDate = LocalDate.parse(dateStr, formatter)

        // Get the current date
        val currentDate = LocalDate.now()

        // Return true if the given date is before the current date
        return givenDate.isBefore(currentDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isDateBeforeCurrentWithTime(dateStr: String): Boolean {
        // Extract only the date part if time is present
        val dateOnly = dateStr.substring(0, 10) // First 10 chars: "yyyy-MM-dd"

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val givenDate = LocalDate.parse(dateOnly, formatter)
        val currentDate = LocalDate.now()

        return givenDate.isBefore(currentDate)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun isCurrentDate(dateTime: String): Boolean {
        val dateOnly = dateTime.substring(0, 10) // "2025-07-21"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val givenDate = LocalDate.parse(dateOnly, formatter)
        val currentDate = LocalDate.now()

        return givenDate.isEqual(currentDate)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun isDateBeforeOrOnCurrent(dateStr: String): Boolean {
        // Define the date format (adjust as necessary to match your input format)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // Parse the input date string to a LocalDate
        val givenDate = LocalDate.parse(dateStr, formatter)

        // Get the current date
        val currentDate = LocalDate.now()

        // Return true if the given date is before or the same as the current date
        return !givenDate.isAfter(currentDate)
    }


    fun parseDate(date: String): String {

        val timeExtract = date.split("T")
        return timeExtract[0]
    }

    fun isEndTimeValid(startTime: List<Int>, endTime: List<Int>): Boolean {
        val startHour = startTime[0]
        val startMinute = startTime[1]
        val endHour = endTime[0]
        val endMinute = endTime[1]

        return if (endHour > startHour) {
            true
        } else endHour == startHour && endMinute > startMinute
    }


    fun saveLocationDataList(context: Context, newLocationData: LocationList) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.LOCATION_LIST, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()

        // Retrieve the existing list
        val currentJson = sharedPreferences.getString(Constants.LOCATION_LIST, null)
        val type = object : TypeToken<List<LocationList>>() {}.type
        val currentList: MutableList<LocationList> = gson.fromJson(currentJson, type) ?: mutableListOf()

        // Add new data to the list
        currentList.add(newLocationData)

        // Save the updated list back to SharedPreferences
        val updatedJson = gson.toJson(currentList)
        editor.putString(Constants.LOCATION_LIST, updatedJson)
        editor.apply()
    }

    fun getLocationDataList(context: Context): List<LocationList>? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.LOCATION_LIST, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(Constants.LOCATION_LIST, null)
        val type = object : TypeToken<List<LocationList>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearLocationDataList(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.LOCATION_LIST, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(Constants.LOCATION_LIST)
        editor.apply()
    }

    // Clear all values
    fun clearAllValues(context: Context,key: String) {
        val sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }


    fun handleSamplingAndRotationBitmap(bitmap: Bitmap): Bitmap? {
        var rotatedBitmap: Bitmap? = bitmap
        try {
            rotatedBitmap = rotateImageIfRequired(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotatedBitmap
    }

     fun rotateImageIfRequired(bitmap: Bitmap): Bitmap? {
        val matrix = Matrix()
        val orientation = ExifInterface.ORIENTATION_NORMAL
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

     fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    suspend fun loadBitmapFromUri(uri: Uri?,context: Context): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                uri?.let {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val options = BitmapFactory.Options().apply {
                        // Set inJustDecodeBounds to true to obtain bitmap dimensions without loading it into memory
                        inJustDecodeBounds = true
                    }
                    // Decode bitmap dimensions
                    BitmapFactory.decodeStream(inputStream, null, options)
                    inputStream?.close()

                    // Calculate sample size to scale down the bitmap if necessary
                    val requiredWidth = 800 // Adjust as needed
                    val requiredHeight = 800 // Adjust as needed
                    options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight)

                    // Set inJustDecodeBounds to false to load the actual bitmap
                    options.inJustDecodeBounds = false

                    // Load bitmap with adjusted options
                    val bitmapStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(bitmapStream, null, options)
                    bitmapStream?.close()

                    return@withContext bitmap
                }
                null
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } catch (e: SecurityException) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun scheduleServiceStop(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        // Set up the calendar for 12 AM
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If the time is already past, set it for the next day
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, StopocationService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set the alarm to trigger at 12 AM
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val sp = context.getSharedPreferences(Constants.NOTIFICATION_ALL_READ,AppCompatActivity.MODE_PRIVATE)
            sp.edit().putBoolean(Constants.NOTIFICATION_ALL_READ,false).apply()
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        } else {

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    fun scheduleAutoCheckout(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, AutoCheckoutReceiver::class.java).apply {
            action = Constants.AUTO_CHECK_OUT_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        context.getSharedPreferences(Constants.AUTO_CHECK_OUT_PENDING, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(Constants.AUTO_CHECK_OUT_PENDING, true)
            .apply()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    fun cancelAutoCheckout(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AutoCheckoutReceiver::class.java).apply {
            action = Constants.AUTO_CHECK_OUT_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        context.getSharedPreferences(Constants.AUTO_CHECK_OUT_PENDING, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(Constants.AUTO_CHECK_OUT_PENDING, false)
            .apply()
    }

    fun restorePendingAutoCheckout(context: Context) {
        val isPending = context.getSharedPreferences(Constants.AUTO_CHECK_OUT_PENDING, Context.MODE_PRIVATE)
            .getBoolean(Constants.AUTO_CHECK_OUT_PENDING, false)
        val isCheckedIn = context.getSharedPreferences(Constants.IS_CHECKEDIN, Context.MODE_PRIVATE)
            .getString(Constants.IS_CHECKEDIN, "NO")
        val autoCheckInTime = context.getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, Context.MODE_PRIVATE)
            .getString(Constants.AUTO_CHECK_IN_TIME, null)

        if (isPending && isCheckedIn == "YES" && !autoCheckInTime.isNullOrEmpty()) {
            scheduleAutoCheckout(context)
        }
    }

    fun saveUriToSharedPref(context: Context, key: String, uri: Uri) {
        val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, uri.toString())
        editor.apply()
    }

    fun getUriFromSharedPref(context: Context, key: String): Uri? {
        val sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        val uriString = sharedPreferences.getString(key, null)
        return uriString?.let { Uri.parse(it) }
    }

    private fun getFileName(uri: Uri,context: Context): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    fileName = it.getString(displayNameIndex)
                }
            }
        }
        return fileName
    }

    fun extractFileNameFromUrl(url: String): String? {
        val uri = Uri.parse(url)
        return uri.lastPathSegment
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimeToGmt(inputTimeString: String): String {
        // Define the input time pattern
        val inputPattern = DateTimeFormatter.ofPattern("HH:mm")

        return try {
            // Parse the input time string to LocalTime
            val localTime = LocalTime.parse(inputTimeString, inputPattern)

            // Get the current date
            val currentDate = LocalDate.now()

            // Combine current date with the input time
            val localDateTime = LocalDateTime.of(currentDate, localTime)

            // Convert LocalDateTime to ZonedDateTime in the system's default time zone
            val zonedDateTime = localDateTime.atZone(ZoneId.systemDefault())

            // Convert ZonedDateTime to GMT/UTC
            val gmtTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC)

            // Extract the time part only and format it as "HH:mm"
            val outputPattern = DateTimeFormatter.ofPattern("HH:mm")
            gmtTime.toLocalTime().format(outputPattern)
        } catch (e: DateTimeParseException) {
            "Invalid time format"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isNotFutureDate(startTime: String):Boolean{

        val extractDate = startTime
        val dateArr = extractDate.split(" ")
        val date = dateArr[0]

        if (CommonMethods.isDateBeforeOrOnCurrent(date)) return true

        return false
    }

    fun setStatusBarColor(
        window: Window,
        applicationContext: Context,
        root: ConstraintLayout,
        activity: Activity
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.profile_gradient_first_color))
                view.setPadding(0, statusBarInsets.top, 0, 0)
                insets
            }


            // Apply fitsSystemWindows behavior dynamically
//            ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
//                val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
//                view.setPadding(0, statusBarInsets, 0, 0) // Avoids overlapping
//                insets
//            }
        }
//        else {
//            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.profile_gradient_first_color)
//        }
    }

}
