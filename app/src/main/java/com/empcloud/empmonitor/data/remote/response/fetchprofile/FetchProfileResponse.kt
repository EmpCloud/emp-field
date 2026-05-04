package com.empcloud.empmonitor.data.remote.response.fetchprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FetchProfileResponse(
    @SerializedName("statusCode")
    val code: Int,
    @SerializedName("body")
    val body: FetchProfileBody
):Serializable
