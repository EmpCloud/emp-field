package com.empcloud.empmonitor.data.remote.response.holidays

import com.empcloud.empmonitor.data.remote.response.login.InnerData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HolidaysResponse(

    val statusCode: Int,
    val body: HolidayBody
):Serializable
