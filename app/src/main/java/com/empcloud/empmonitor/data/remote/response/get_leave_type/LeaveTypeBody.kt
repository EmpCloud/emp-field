package com.empcloud.empmonitor.data.remote.response.get_leave_type

import java.io.Serializable

data class LeaveTypeBody(
    val status:String,
    val message:String,
    val data:LeaveTypeObject
):Serializable
