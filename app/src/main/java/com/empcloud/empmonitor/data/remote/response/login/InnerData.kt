package com.empcloud.empmonitor.data.remote.response.login

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InnerData(
    @SerializedName("userData")
    val userData: UserData,
    @SerializedName("accessToken")
    val accessToken: String
):Serializable
