package com.empcloud.empmonitor.utils.broadcast_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AutoCheckoutReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: NetworkRepository

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Constants.AUTO_CHECK_OUT_ACTION) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authToken = context.getSharedPreferences(Constants.AUTH_TOKEN, Context.MODE_PRIVATE)
                    .getString(Constants.AUTH_TOKEN, "") ?: ""
                val isCheckedIn = context.getSharedPreferences(Constants.IS_CHECKEDIN, Context.MODE_PRIVATE)
                    .getString(Constants.IS_CHECKEDIN, "NO")
                val autoCheckInTime = context.getSharedPreferences(Constants.AUTO_CHECK_IN_TIME, Context.MODE_PRIVATE)
                    .getString(Constants.AUTO_CHECK_IN_TIME, null)

                if (authToken.isEmpty() || isCheckedIn != "YES" || autoCheckInTime.isNullOrEmpty()) {
                    CommonMethods.cancelAutoCheckout(context)
                    return@launch
                }

                val lastTrackedLocation = CommonMethods.getLocationDataList(context)?.lastOrNull()
                val latitude = lastTrackedLocation?.latitude
                    ?: context.getSharedPreferences(Constants.ORG_LATITUDE, Context.MODE_PRIVATE)
                        .getString(Constants.ORG_LATITUDE, null)
                        ?.toDoubleOrNull()
                    ?: 0.0
                val longitude = lastTrackedLocation?.longitude
                    ?: context.getSharedPreferences(Constants.ORG_LONGITUDE, Context.MODE_PRIVATE)
                        .getString(Constants.ORG_LONGITUDE, null)
                        ?.toDoubleOrNull()
                    ?: 0.0

                val markAttendanceModel = MarkAttendanceModel(
                    CommonMethods.getCurrentTime(),
                    latitude,
                    longitude
                )

                repository.getMarkAttendanceData(authToken, markAttendanceModel).collect { response ->
                    when (response) {
                        is ApiState.SUCESS -> {
                            val apiResponse = response.getResponse
                            val message = apiResponse.body.data.message
                            if (apiResponse.statusCode == 200 && message == "Successfully Checked Out") {
                                Log.d("AutoCheckIn", "Auto checkout successful at midnight")
                                CommonMethods.clearStringFromSharedPreferences(context, Constants.IS_CHECKEDIN)
                                context.getSharedPreferences(Constants.IS_CHECKEDIN, Context.MODE_PRIVATE)
                                    .edit()
                                    .putString(Constants.IS_CHECKEDIN, "NO")
                                    .apply()
                                context.getSharedPreferences(Constants.IS_CHECKED_OUT, Context.MODE_PRIVATE)
                                    .edit()
                                    .putString(Constants.IS_CHECKED_OUT, "YES")
                                    .apply()
                                CommonMethods.clearStringFromSharedPreferences(context, Constants.AUTO_CHECK_IN_TIME)
                                CommonMethods.cancelAutoCheckout(context)
                                CommonMethods.clearLocationDataList(context)
                                context.stopService(Intent(context, LocationService::class.java))
                            } else {
                                Log.d("AutoCheckIn", "Auto checkout API did not complete: statusCode=${apiResponse.statusCode}, message=$message")
                            }
                        }

                        is ApiState.ERROR -> {
                            Log.d("AutoCheckIn", "Auto checkout failed")
                        }

                        else -> {}
                    }
                }
            } catch (e: Exception) {
                Log.e("AutoCheckIn", "Auto checkout receiver error: ${e.message}", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
