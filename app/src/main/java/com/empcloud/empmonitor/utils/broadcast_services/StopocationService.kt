package com.empcloud.empmonitor.utils.broadcast_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.empcloud.empmonitor.ui.services.location_tacking.LocationService
import com.empcloud.empmonitor.utils.CommonMethods
import com.empcloud.empmonitor.utils.Constants

class StopocationService :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED){

            context.let {
                CommonMethods.scheduleServiceStop(context!!)
                CommonMethods.restorePendingAutoCheckout(context)

            }
        }
        context.let {
            val serviceIntent = Intent(it,LocationService::class.java)
            it?.stopService(serviceIntent)
        }

    }
}
