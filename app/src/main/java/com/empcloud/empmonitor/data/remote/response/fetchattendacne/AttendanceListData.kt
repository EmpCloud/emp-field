package com.empcloud.empmonitor.data.remote.response.fetchattendacne

import java.io.Serializable

data class AttendanceListData(
    val code:Int,
    val message:String,
    val error:String,
    val data: AttInnerDataResponse
):Serializable
