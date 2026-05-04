package com.empcloud.empmonitor.data.remote.response.attendance

import java.io.Serializable

data class UserAttendanceData(
    val attendance:List<AttendanceFullData>
):Serializable
