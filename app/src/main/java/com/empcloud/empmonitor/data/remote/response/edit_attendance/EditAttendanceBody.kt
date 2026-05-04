package com.empcloud.empmonitor.data.remote.response.edit_attendance

import java.io.Serializable

data class EditAttendanceBody(

    val status:String,
    val message:String,
    val data:EditAttendanceData
):Serializable
