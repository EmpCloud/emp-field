package com.empcloud.empmonitor.utils.broadcast_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.device_status.DeviceStatusLogger

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val hasInternet = isInternetAvailable(context)
        if (hasInternet && canRunTracking(context)) {
            val serviceIntent = Intent(context, LocationService::class.java)
            context.startService(serviceIntent)
        } else if (hasInternet) {
            DeviceStatusLogger.d("location upload skipped because user not checked in")
        }
    }

    private fun canRunTracking(context: Context): Boolean {
        val token = context.getSharedPreferences(Constants.AUTH_TOKEN, Context.MODE_PRIVATE)
            .getString(Constants.AUTH_TOKEN, "") ?: ""
        val isCheckedIn = context.getSharedPreferences(Constants.IS_CHECKEDIN, Context.MODE_PRIVATE)
            .getString(Constants.IS_CHECKEDIN, "NO") == "YES"
        return token.isNotEmpty() && isCheckedIn
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
