package com.empcloud.empmonitor.data.remote.response.fetchattendacne

import java.io.Serializable

data class EmployeeLocation(
    val _id: String,
    val latitude: String,
    val longitude: String,
    val geo_fencing: Boolean,
    val range: Int,
    val isMobEnabled: Boolean,
    val address: String?,
    val description: String?
) : Serializable
