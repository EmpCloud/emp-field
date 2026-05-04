package com.empcloud.empmonitor.data.remote.request.createleave

import java.io.Serializable

data class CreateLeaveRequestModel(
    val day_type:Int,
    val leave_type:Int,
    val start_date:String,
    val end_date:String,
    val reason:String

    ):Serializable
