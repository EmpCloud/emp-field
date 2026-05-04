package com.empcloud.empmonitor.data.remote.response.createlevae

import java.io.Serializable

data class CreateLeaveResponse(
    val statusCode:Int,
    val body:CreateLeaveBody
):Serializable
