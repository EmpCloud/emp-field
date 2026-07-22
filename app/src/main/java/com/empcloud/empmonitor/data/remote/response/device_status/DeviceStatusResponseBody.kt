package com.empcloud.empmonitor.data.remote.response.device_status

import java.io.Serializable

data class DeviceStatusResponseBody(
    val status: String? = null,
    val message: String? = null,
    val data: DeviceStatusData? = null
) : Serializable
