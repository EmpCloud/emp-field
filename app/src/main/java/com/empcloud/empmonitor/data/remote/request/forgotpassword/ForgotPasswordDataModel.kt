package com.empcloud.empmonitor.data.remote.request.forgotpassword

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ForgotPasswordDataModel(
    @SerializedName("email")
    val email:String
):Serializable
