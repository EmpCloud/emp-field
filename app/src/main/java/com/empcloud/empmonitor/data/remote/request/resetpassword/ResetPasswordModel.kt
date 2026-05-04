package com.empcloud.empmonitor.data.remote.request.resetpassword

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResetPasswordModel(
    @SerializedName("email")
    val email:String,
    @SerializedName("verifyToken")
    val OTP:String,
    @SerializedName("newPassword")
    val newpassword:String

    ):Serializable
