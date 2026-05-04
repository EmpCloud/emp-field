package com.empcloud.empmonitor.data.remote.request.fetchprofile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FetchProfileModel(
    @SerializedName("accessToken")
    val accessToken:String
):Serializable
