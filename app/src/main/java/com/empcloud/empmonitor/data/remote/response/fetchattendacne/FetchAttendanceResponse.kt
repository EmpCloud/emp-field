package com.empcloud.empmonitor.data.remote.response.fetchattendacne

import com.empcloud.empmonitor.data.remote.response.markattendance.AttendanceBody
import java.io.Serializable

data class FetchAttendanceResponse(

    val statusCode:Int,
    val body: AttendanceBodyResponse
):Serializable
