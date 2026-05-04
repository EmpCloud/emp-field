package com.empcloud.empmonitor.data.remote.response.login

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponseBody(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: InnerData
):Serializable
