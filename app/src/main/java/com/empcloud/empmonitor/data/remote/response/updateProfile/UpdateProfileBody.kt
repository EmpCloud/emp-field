package com.empcloud.empmonitor.data.remote.response.uploadprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateProfileBody(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UpdateDataFetch
):Serializable
