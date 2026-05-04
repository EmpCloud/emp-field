package com.empcloud.empmonitor.data.remote.response.fetchprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FetchProfileBody(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: DataFetch
):Serializable
