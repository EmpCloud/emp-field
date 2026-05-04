package com.empcloud.empmonitor.data.remote.request.attendance

import java.io.Serializable

data class AttendanceRequestModel(
    val startDate:String,
    val endDate:String
):Serializable
