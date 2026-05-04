package com.empcloud.empmonitor.data.remote.response.mobilelogin

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MobileLoginResponse(
    @SerializedName("statusCode")
    val code: Int,
    @SerializedName("body")
    val body : MobileLoginResponseBody
):Serializable {

}
