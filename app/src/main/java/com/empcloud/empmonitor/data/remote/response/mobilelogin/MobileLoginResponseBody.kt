package com.empcloud.empmonitor.data.remote.response.mobilelogin

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MobileLoginResponseBody(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: MobileInnerData
):Serializable
