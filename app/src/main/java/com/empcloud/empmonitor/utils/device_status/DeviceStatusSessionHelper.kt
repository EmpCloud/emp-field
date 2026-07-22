package com.empcloud.empmonitor.utils.device_status

import android.content.Context
import android.content.Intent
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.ui.activity.login.LoginOptionsActivity
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants

object DeviceStatusSessionHelper {
    private const val SESSION_EXPIRED_MESSAGE = "Session expired. Logged in on another device."

    fun isSessionExpired(error: ApiState.ERROR): Boolean {
        if (error.errorCode == 401) return true
        if (error.message.contains(SESSION_EXPIRED_MESSAGE, ignoreCase = true)) return true
        val errorBody = try {
            error.errorBody?.string()
        } catch (_: Exception) {
            null
        }
        return errorBody?.contains(SESSION_EXPIRED_MESSAGE, ignoreCase = true) == true
    }

    fun isSessionExpired(statusCode: Int?, message: String?): Boolean {
        return statusCode == 401 ||
                message?.contains(SESSION_EXPIRED_MESSAGE, ignoreCase = true) == true
    }

    fun isHttpSessionExpired(httpCode: Int, errorBody: String?): Boolean {
        return httpCode == 401 ||
                errorBody?.contains(SESSION_EXPIRED_MESSAGE, ignoreCase = true) == true
    }

    fun handleSessionExpired(context: Context) {
        val appContext = context.applicationContext
        DeviceStatusLogger.d("session expired from device-status endpoint; logging out")
        DeviceStatusHeartbeatScheduler.cancel(appContext)
        CommonMethods.cancelAutoCheckout(appContext)
        CommonMethods.clearLocationDataList(appContext)
        appContext.stopService(Intent(appContext, LocationService::class.java))
        CommonMethods.clearStringFromSharedPreferences(appContext, Constants.AUTH_TOKEN)
        CommonMethods.clearStringFromSharedPreferences(appContext, Constants.IS_CHECKEDIN)
        appContext.getSharedPreferences(Constants.IS_CHECKEDIN, Context.MODE_PRIVATE)
            .edit()
            .putString(Constants.IS_CHECKEDIN, "NO")
            .apply()
        CommonMethods.clearStringFromSharedPreferences(appContext, Constants.AUTO_CHECK_IN_TIME)
        CommonMethods.clearStringFromSharedPreferences(appContext, Constants.CHECK_IN_METHOD)
        val intent = Intent(appContext, LoginOptionsActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        appContext.startActivity(intent)
    }
}
