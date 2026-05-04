package com.empcloud.empmonitor.utils.broadcast_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.view.LayoutInflater
import android.view.View
import com.empcloud.empmonitor.R

class BatteryLevelReciever(private val context:Context):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val level = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
        val scale = intent!!.getIntExtra(BatteryManager.EXTRA_SCALE,-1)
        val batteryPct = level / scale?.toFloat()!! * 100

        if (batteryPct <= 15){

            showLowBatteryAlert(context!!)

        }
    }

    private fun showLowBatteryAlert(context: Context) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.batterydrainalert, null)

        view.visibility = View.VISIBLE
    }
}