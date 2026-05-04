package com.empcloud.empmonitor.data.remote.response.attendance

import java.io.Serializable

data class AttendanceResponse(
    val statusCode:Int,
    val body:AttendanceBodyFetch
):Serializable
