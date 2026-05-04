package com.empcloud.empmonitor.data.remote.response.resetpassword

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResetPasswordResponse(
    @SerializedName("statusCode")
    val code:Int,
    @SerializedName("body")
    val body: ResetPasswordBody
):Serializable
