package com.empcloud.empmonitor.data.remote.response.resetpassword

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResetPasswordBody(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String
):Serializable
