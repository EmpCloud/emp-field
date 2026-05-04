package com.empcloud.empmonitor.data.remote.response.get_leave_type

import java.io.Serializable

data class GetLeaveTypeResponse(

    val statusCode:Int,
    val body:LeaveTypeBody
):Serializable
