package com.empcloud.empmonitor.data.remote.response.markattendance

import com.empcloud.empmonitor.data.remote.response.login.InnerData
import java.io.Serializable

data class AttendanceData(
    val code:Int,
    val message:String,
    val error:String,
    val data:AttInnerData
):Serializable
