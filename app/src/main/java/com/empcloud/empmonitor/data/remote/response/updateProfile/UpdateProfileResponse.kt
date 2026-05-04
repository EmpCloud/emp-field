package com.empcloud.empmonitor.data.remote.response.uploadprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateProfileResponse(
    @SerializedName("statusCode")
    val code: Int,
    @SerializedName("body")
    val body: UpdateProfileBody
):Serializable
