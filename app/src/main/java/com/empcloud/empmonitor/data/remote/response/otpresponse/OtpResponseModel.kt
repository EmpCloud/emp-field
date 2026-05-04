package com.empcloud.empmonitor.data.remote.response.otpresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OtpResponseModel(
    @SerializedName("statusCode")
    val statusCode:Int,
    @SerializedName("body")
    val body:OtpBody
):Serializable
