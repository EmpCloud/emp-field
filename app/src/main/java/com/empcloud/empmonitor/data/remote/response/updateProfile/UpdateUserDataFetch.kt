package com.empcloud.empmonitor.data.remote.response.uploadprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateUserDataFetch(

    @SerializedName("id")
    val id: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("profilePic")
    val profilePic: String?,
    @SerializedName("address1")
    val address1: String?,
    @SerializedName("address2")
    val address2: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("zipCode")
    val zipCode: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String

):Serializable
