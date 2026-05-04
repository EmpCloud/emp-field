package com.empcloud.empmonitor.data.remote.response.holidays

import com.empcloud.empmonitor.data.remote.response.login.InnerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HolidayBody(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<HolidayList>
):Serializable
