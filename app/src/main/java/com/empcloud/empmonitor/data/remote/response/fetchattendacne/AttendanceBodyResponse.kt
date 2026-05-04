package com.empcloud.empmonitor.data.remote.response.fetchattendacne

import com.empcloud.empmonitor.data.remote.response.markattendance.AttendanceData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttendanceBodyResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: AttendanceDataResponse
):Serializable
