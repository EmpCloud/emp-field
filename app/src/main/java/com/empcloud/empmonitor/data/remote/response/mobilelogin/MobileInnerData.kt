package com.empcloud.empmonitor.data.remote.response.mobilelogin

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MobileInnerData(
    @SerializedName("userData")
    val userData: MobileUserData,
    @SerializedName("accessToken")
    val accessToken: String
):Serializable
