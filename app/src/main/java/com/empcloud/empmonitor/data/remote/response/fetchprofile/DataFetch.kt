package com.empcloud.empmonitor.data.remote.response.fetchprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataFetch(
    @SerializedName("resultData")
    val resultData: List<UserDataFetch>
):Serializable
