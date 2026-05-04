package com.empcloud.empmonitor.data.remote.response.attendance

import java.io.Serializable

data class Attendance(
    val date:String,
    val status:Int,
    val half_day:Int,
    val day_off:Boolean,
    val start_time:String? = null,
    val end_time:String? = null
):Serializable
