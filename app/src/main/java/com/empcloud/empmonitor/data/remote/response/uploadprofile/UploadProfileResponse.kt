package com.empcloud.empmonitor.data.remote.response.uploadprofile

import java.io.Serializable

data class UploadProfileResponse(
    val statusCode:Int,
    val body:UploadResponse
):Serializable
