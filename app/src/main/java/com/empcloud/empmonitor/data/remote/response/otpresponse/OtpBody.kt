package com.empcloud.empmonitor.data.remote.response.otpresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OtpBody(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String
):Serializable
