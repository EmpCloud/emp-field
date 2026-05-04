package com.empcloud.empmonitor.data.remote.request.login

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginDataModel(
    @SerializedName("userMail")
    val userMail:String,
    @SerializedName("password")
    val password:String
):Serializable
