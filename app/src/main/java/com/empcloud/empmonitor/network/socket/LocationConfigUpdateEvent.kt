package com.empcloud.empmonitor.network.socket

data class LocationConfigUpdateEvent(
    val orgId: String,
    val locationName: String?,
    val isGlobalUpdate: Boolean,
    val updatedAt: String?,
    val message: String?
)
