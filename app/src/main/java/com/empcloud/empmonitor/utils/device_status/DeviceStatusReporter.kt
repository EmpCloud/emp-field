package com.empcloud.empmonitor.utils.device_status

import android.content.Context
import com.empcloud.empmonitor.data.remote.request.device_status.DeviceStatusRequest
import com.empcloud.empmonitor.network.ApiService
import com.empcloud.empmonitor.utils.Constants
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withContext

enum class DeviceStatusSendResult {
    SENT,
    SKIPPED,
    FAILED,
    SESSION_EXPIRED
}

object DeviceStatusReporter {
    const val CHECKOUT_TIMEOUT_MS = 10_000L
    private const val DEFAULT_TIMEOUT_MS = 30_000L

    suspend fun sendHeartbeat(
        context: Context,
        status: String? = null,
        requireCheckedIn: Boolean = true,
        requestTimeoutMs: Long = DEFAULT_TIMEOUT_MS
    ): DeviceStatusSendResult = withContext(Dispatchers.IO) {
        val appContext = context.applicationContext
        val token = appContext.getSharedPreferences(Constants.AUTH_TOKEN, Context.MODE_PRIVATE)
            .getString(Constants.AUTH_TOKEN, "") ?: ""
        val isCheckedIn = appContext.getSharedPreferences(Constants.IS_CHECKEDIN, Context.MODE_PRIVATE)
            .getString(Constants.IS_CHECKEDIN, "NO") == "YES"

        if (token.isEmpty()) {
            DeviceStatusLogger.d("heartbeat skipped because auth token is missing")
            return@withContext DeviceStatusSendResult.SKIPPED
        }

        val mustBeCheckedIn = requireCheckedIn && status != Constants.DEVICE_STATUS_INACTIVE
        if (mustBeCheckedIn && !isCheckedIn) {
            DeviceStatusLogger.d("heartbeat skipped because user not checked in")
            return@withContext DeviceStatusSendResult.SKIPPED
        }

        val battery = DeviceBatteryReader.read(appContext)
        val request = DeviceStatusRequest(
            batteryPercent = battery.batteryPercent,
            isCharging = battery.isCharging,
            status = status
        )

        if (status == Constants.DEVICE_STATUS_INACTIVE) {
            DeviceStatusLogger.d(
                "checkout inactive status sent batteryPercent=${request.batteryPercent}, isCharging=${request.isCharging}"
            )
        } else {
            DeviceStatusLogger.d(
                "heartbeat sent batteryPercent=${request.batteryPercent}, isCharging=${request.isCharging}"
            )
        }

        try {
            val response = withTimeout(requestTimeoutMs) {
                apiService(appContext).updateDeviceStatus(token, request)
            }
            val body = response.body()
            if (response.isSuccessful) {
                if (DeviceStatusSessionHelper.isSessionExpired(body?.statusCode, body?.resolvedMessage)) {
                    DeviceStatusSessionHelper.handleSessionExpired(appContext)
                    return@withContext DeviceStatusSendResult.SESSION_EXPIRED
                }
                DeviceStatusLogger.d(
                    "heartbeat success status=${body?.resolvedStatus}, message=${body?.resolvedMessage}"
                )
                DeviceStatusSendResult.SENT
            } else {
                val errorBody = response.errorBody()?.string()
                if (DeviceStatusSessionHelper.isHttpSessionExpired(response.code(), errorBody)) {
                    DeviceStatusSessionHelper.handleSessionExpired(appContext)
                    return@withContext DeviceStatusSendResult.SESSION_EXPIRED
                }
                DeviceStatusLogger.d(
                    "heartbeat failure http=${response.code()}, message=${errorBody ?: response.message()}"
                )
                DeviceStatusSendResult.FAILED
            }
        } catch (e: Exception) {
            DeviceStatusLogger.e("heartbeat failure exception=${e.message}", e)
            DeviceStatusSendResult.FAILED
        }
    }

    private fun apiService(context: Context): ApiService {
        return EntryPointAccessors.fromApplication(
            context.applicationContext,
            DeviceStatusApiEntryPoint::class.java
        ).apiService()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DeviceStatusApiEntryPoint {
    fun apiService(): ApiService
}
