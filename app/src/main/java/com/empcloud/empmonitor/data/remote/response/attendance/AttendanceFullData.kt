package com.empcloud.empmonitor.data.remote.response.attendance

import com.empcloud.empmonitor.utils.CommonDetailsDeserializer
import com.google.gson.annotations.JsonAdapter
import java.io.Serializable

data class AttendanceFullData(
//   val attendance:List<Attendance>
    val date:String,
    val status:Int,
    val half_day:Int,
    val day_off:Boolean,
    val start_time:String?,
    val end_time:String?,
    val holiday_name:String?,
    val leave_name:String?,
    val leave_type:Int?,
    val half_day_status:Int?,
    val half_day_leave:String?,
    val logged_duration:Int,
    val min_hours:List<MinHourBody>,
    @JsonAdapter(CommonDetailsDeserializer::class)
    val open_request:AObjectDetails,
    @JsonAdapter(CommonDetailsDeserializer::class)
    val open_attendance_request:BObjectDetails

    ):Serializable

data class AObjectDetails(
    val leave_name: String,
    val employee_leave_type:Int,
    val day_type:Int
):Serializable

data class BObjectDetails(
    val request_status: String?
):Serializable