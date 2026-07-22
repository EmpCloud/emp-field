package com.empcloud.empmonitor.utils.device_status

import android.util.Log
import com.empcloud.empmonitor.utils.Constants

object DeviceStatusLogger {
    fun d(message: String) {
        Log.d(Constants.DEVICE_STATUS_LOG_TAG, "${Constants.DEVICE_STATUS_LOG_PREFIX} $message")
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (throwable == null) {
            Log.e(Constants.DEVICE_STATUS_LOG_TAG, "${Constants.DEVICE_STATUS_LOG_PREFIX} $message")
        } else {
            Log.e(Constants.DEVICE_STATUS_LOG_TAG, "${Constants.DEVICE_STATUS_LOG_PREFIX} $message", throwable)
        }
    }
}
