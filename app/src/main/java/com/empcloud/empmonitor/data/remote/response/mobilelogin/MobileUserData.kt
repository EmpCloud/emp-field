package com.empcloud.empmonitor.data.remote.response.mobilelogin

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MobileUserData(
    @SerializedName("id")
    val id: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("profilePic")
    val profilePic: String,
    @SerializedName("userType")
    val userType: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("isEmpMonitorAdmin")
    val isEmpMonitorAdmin: String,
    @SerializedName("isTwoFactorEnabled")
    val isTwoFactorEnabled: String,
    @SerializedName("orgId")
    val orgId: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("zipCode")
    val zipCode: String,
    @SerializedName("countryCode")
    val countryCode: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("phoneVerified")
    val phoneVerified: String,
    @SerializedName("emailVerified")
    val emailVerified: String,
    @SerializedName("invitation")
    val invitation: String,
    @SerializedName("isSuspended")
    val isSuspended: String,
    @SerializedName("empMonitor")
    val empMonitor: String,
    @SerializedName("numberValidateOtp")
    val numberValidateOtp: String,
    @SerializedName("numberValidateOtpExpire")
    val numberValidateOtpExpire: String,
    @SerializedName("emailValidateToken")
    val emailValidateToken: String,
    @SerializedName("emailTokenExpire")
    val emailTokenExpire: String,
    @SerializedName("verificationEmailSentCount")
    val verificationEmailSentCount: String,
    @SerializedName("forgotPasswordToken")
    val forgotPasswordToken: String,

    val emp_id:String,
    @SerializedName("forgotTokenExpire")
    val forgotTokenExpire: String,
    @SerializedName("passwordEmailSentCount")
    val passwordEmailSentCount: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("__v")
    val v: Int,

    val isGlobalUser:Boolean

):Serializable
