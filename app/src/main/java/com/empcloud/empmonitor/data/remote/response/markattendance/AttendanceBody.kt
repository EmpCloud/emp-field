package com.empcloud.empmonitor.data.remote.response.markattendance

import com.empcloud.empmonitor.data.remote.response.fetchprofile.DataFetch
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttendanceBody(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: AttendanceData
):Serializable

//@SerializedName("data")
//val data: AttendanceData