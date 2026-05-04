package com.empcloud.empmonitor.data.remote.request.editleave

import java.io.Serializable

data class EditLeaveRequestModel(
    val leave_id:Int,
    val day_type:Int,
    val leave_type:Int,
    val start_date:String,
    val end_date:String,
    val reason:String
):Serializable
