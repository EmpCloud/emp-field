package com.empcloud.empmonitor.data.remote.response.tracking_settings

import java.io.Serializable

data class TrackingSettingsBody(
    val status: String,
    val message: String,
    val data: TrackingSettingsData
) : Serializable
