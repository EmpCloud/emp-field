package com.empcloud.empmonitor.data.remote.response.device_status

import java.io.Serializable

data class DeviceStatusResponse(
    val statusCode: Int? = null,
    val body: DeviceStatusResponseBody? = null,
    val status: String? = null,
    val message: String? = null,
    val data: DeviceStatusData? = null
) : Serializable {
    val resolvedStatus: String?
        get() = body?.status ?: status

    val resolvedMessage: String?
        get() = body?.message ?: message

    val resolvedData: DeviceStatusData?
        get() = body?.data ?: data
}
