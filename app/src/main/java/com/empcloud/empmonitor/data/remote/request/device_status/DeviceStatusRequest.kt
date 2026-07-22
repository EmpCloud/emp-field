package com.empcloud.empmonitor.data.remote.request.device_status

import java.io.Serializable

data class DeviceStatusRequest(
    val batteryPercent: Int?,
    val isCharging: Boolean?,
    val status: String? = null
) : Serializable
