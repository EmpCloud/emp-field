package com.empcloud.empmonitor.data.remote.response.tracking_settings

import java.io.Serializable

data class TrackingSettingsData(
    val latitude: String,
    val longitude: String,
    val orgRadius: Int,
    val isBioMetricEnabled: Int,
    val isWebEnabled: Int,
    val geoLogStatus: Boolean,
    val currentFrequency: Int,
    val currentRadius: Int,
    val currentMode: String,
    val isMobileDeviceEnabled: Int,
    val autoCheckInByMobile: Int,
    val isGeoFencingOn: Int,
    val autoCheckInByGeoFencing: Int,
    val employeeLocations: List<EmployeeLocationItem>? = null
) : Serializable
