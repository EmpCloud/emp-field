package com.empcloud.empmonitor.data.remote.response.fetchprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDataFetch(

    @SerializedName("id")
    val id: String,
    @SerializedName("fullName")
    var fullName: String,
    @SerializedName("age")
    var age: String,
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
    val phoneNumber: String,
    val latitude:String,
    val longitude:String

):Serializable
