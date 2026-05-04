package com.empcloud.empmonitor.data.remote.request.mark_attendance

import java.io.Serializable

data class MarkAttendanceModel(
    val time:String,
    val latitude:Double,
    val longitude:Double
):Serializable
