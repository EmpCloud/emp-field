package com.empcloud.empmonitor.data.remote.response.tracking_settings

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EmployeeLocationItem(
    @SerializedName("_id")
    val id: String,
    val latitude: String,
    val longitude: String,
    @SerializedName("geo_fencing")
    val geoFencing: Boolean,
    val range: Int,
    val isMobEnabled: Boolean,
    val address: String,
    val description: String
) : Serializable
