package com.empcloud.empmonitor.data.remote.response.uploadprofile

import java.io.Serializable

data class UploadResponse(
    val status:String,
    val message:String,
    val data:ProfileLink
):Serializable
