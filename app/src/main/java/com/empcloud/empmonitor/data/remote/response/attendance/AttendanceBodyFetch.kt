package com.empcloud.empmonitor.data.remote.response.attendance

import java.io.Serializable

data class AttendanceBodyFetch(
    val status:String,
    val message:String,
    val data:List<UserAttendanceData>
    ):Serializable
