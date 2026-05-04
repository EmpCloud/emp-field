package com.empcloud.empmonitor.data.remote.response.edit_attendance

import java.io.Serializable

data class EditAttendanceResponse(

    val statusCode:Int,
    val body:EditAttendanceBody
):Serializable
