package com.empcloud.empmonitor.data.remote.response.get_leave_type

import java.io.Serializable

data class LeaveTypeObject(
    val code:Int,
    val message:String,
    val data:List<LeaveType>
) :Serializable
