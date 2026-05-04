package com.empcloud.empmonitor.data.remote.response.markattendance

import java.io.Serializable

data class MarkAttendanceResponse(

    val statusCode:Int,
    val body:AttendanceBody
):Serializable
