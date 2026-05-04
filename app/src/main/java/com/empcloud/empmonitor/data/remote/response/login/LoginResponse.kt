package com.empcloud.empmonitor.data.remote.response.login

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("body")
    val body : LoginResponseBody
): Serializable
