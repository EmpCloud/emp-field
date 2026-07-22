package com.empcloud.empmonitor.utils.device_status

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.empcloud.empmonitor.utils.Constants

class DeviceStatusHeartbeatWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val token = context.getSharedPreferences(Constants.AUTH_TOKEN, Context.MODE_PRIVATE)
            .getString(Constants.AUTH_TOKEN, "") ?: ""
        val isCheckedIn = context.getSharedPreferences(Constants.IS_CHECKEDIN, Context.MODE_PRIVATE)
            .getString(Constants.IS_CHECKEDIN, "NO") == "YES"

        if (token.isEmpty()) {
            DeviceStatusLogger.d("heartbeat skipped because auth token is missing")
            return Result.success()
        }

        if (!isCheckedIn) {
            DeviceStatusLogger.d("heartbeat skipped because user not checked in")
            return Result.success()
        }

        val lastLocationUploadAt = DeviceStatusHeartbeatScheduler.lastLocationUploadAt(context)
        val elapsedSinceUpload = System.currentTimeMillis() - lastLocationUploadAt
        if (lastLocationUploadAt > 0 && elapsedSinceUpload < DeviceStatusHeartbeatScheduler.HEARTBEAT_INTERVAL_MS) {
            DeviceStatusLogger.d("heartbeat skipped because recent location upload refreshed lastSeenAt")
            return Result.success()
        }

        return when (DeviceStatusReporter.sendHeartbeat(context)) {
            DeviceStatusSendResult.SENT,
            DeviceStatusSendResult.SKIPPED,
            DeviceStatusSendResult.SESSION_EXPIRED -> Result.success()
            DeviceStatusSendResult.FAILED -> Result.retry()
        }
    }
}
