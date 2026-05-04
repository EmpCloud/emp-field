package com.empcloud.empmonitor.data.remote.response.edit_attendance

import java.io.Serializable

data class EditAttendanceData(
    val code:Int,
    val message:String,
    val error:String
):Serializable
