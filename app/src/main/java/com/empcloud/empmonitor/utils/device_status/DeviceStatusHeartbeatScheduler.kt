package com.empcloud.empmonitor.utils.device_status

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.empcloud.empmonitor.utils.Constants
import java.util.concurrent.TimeUnit

object DeviceStatusHeartbeatScheduler {
    const val HEARTBEAT_INTERVAL_MINUTES = 15L
    const val HEARTBEAT_INTERVAL_MS = HEARTBEAT_INTERVAL_MINUTES * 60_000L
    private const val UNIQUE_WORK_NAME = "device_status_heartbeat"

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<DeviceStatusHeartbeatWorker>(
            HEARTBEAT_INTERVAL_MINUTES,
            TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context.applicationContext).cancelUniqueWork(UNIQUE_WORK_NAME)
    }

    fun markLocationUploadSuccess(context: Context) {
        context.applicationContext
            .getSharedPreferences(Constants.DEVICE_STATUS_PREF, Context.MODE_PRIVATE)
            .edit()
            .putLong(Constants.DEVICE_STATUS_LAST_LOCATION_UPLOAD_AT, System.currentTimeMillis())
            .apply()
    }

    fun lastLocationUploadAt(context: Context): Long {
        return context.applicationContext
            .getSharedPreferences(Constants.DEVICE_STATUS_PREF, Context.MODE_PRIVATE)
            .getLong(Constants.DEVICE_STATUS_LAST_LOCATION_UPLOAD_AT, 0L)
    }
}
