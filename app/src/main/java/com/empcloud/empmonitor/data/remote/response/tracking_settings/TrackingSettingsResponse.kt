package com.empcloud.empmonitor.data.remote.response.tracking_settings

import java.io.Serializable

data class TrackingSettingsResponse(
    val statusCode: Int,
    val body: TrackingSettingsBody
) : Serializable
