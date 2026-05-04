package com.empcloud.empmonitor.data.remote.response.uploadprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateDataFetch(
    @SerializedName("resultData")
    val resultData: List<UpdateUserDataFetch>
):Serializable
