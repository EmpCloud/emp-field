package com.empcloud.empmonitor.utils.broadcast_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants
import com.empcloud.empmonitor.utils.device_status.DeviceStatusHeartbeatScheduler

class StopocationService :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED){

            context.let {
                CommonMethods.scheduleServiceStop(context!!)
                CommonMethods.restorePendingAutoCheckout(context)
                val token = context.getSharedPreferences(Constants.AUTH_TOKEN, Context.MODE_PRIVATE)
                    .getString(Constants.AUTH_TOKEN, "") ?: ""
                val isCheckedIn = context.getSharedPreferences(Constants.IS_CHECKEDIN, Context.MODE_PRIVATE)
                    .getString(Constants.IS_CHECKEDIN, "NO") == "YES"
                if (token.isNotEmpty() && isCheckedIn) {
                    DeviceStatusHeartbeatScheduler.schedule(context)
                }

            }
        }
        context.let {
            val serviceIntent = Intent(it,LocationService::class.java)
            it?.stopService(serviceIntent)
        }

    }
}
