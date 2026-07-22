package com.empcloud.empmonitor.utils.device_status

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

object DeviceBatteryReader {
    fun read(context: Context): DeviceBatterySnapshot {
        val batteryManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(BatteryManager::class.java)
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        }

        val rawPercent = batteryManager
            ?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            ?.takeIf { it != Int.MIN_VALUE }

        val isCharging = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && batteryManager != null) {
            batteryManager.isCharging
        } else {
            readChargingFromStickyBroadcast(context)
        }

        return DeviceBatterySnapshot.fromRaw(rawPercent, isCharging)
    }

    private fun readChargingFromStickyBroadcast(context: Context): Boolean? {
        val intent: Intent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        ) ?: return null

        return when (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
            BatteryManager.BATTERY_STATUS_CHARGING,
            BatteryManager.BATTERY_STATUS_FULL -> true
            BatteryManager.BATTERY_STATUS_DISCHARGING,
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> false
            else -> null
        }
    }
}
