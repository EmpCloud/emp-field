package com.empcloud.empmonitor.data.remote.response.leaves

import java.io.Serializable

data class LeavesList(
    val id:Int,
    val emp_id:String,
    val employee_id:Int,
    val employee_name:String,
    val start_date:String,
    val end_date:String,
    val status:Int,
    val number_of_days:String,
    val day_type:Int,
    val leave_type:Int,
    val name:String,
    val reason:String,
    val day_status:String,
    val leave_status:LeaveStatus
    ):Serializable
