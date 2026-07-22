package com.empcloud.empmonitor.data.remote.response.device_status

import java.io.Serializable

data class DeviceStatusData(
    val emp_id: String? = null,
    val deviceStatus: String? = null,
    val batteryPercent: Int? = null,
    val isCharging: Boolean? = null,
    val lastSeenAt: String? = null
) : Serializable
