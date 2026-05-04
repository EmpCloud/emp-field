package com.empcloud.empmonitor.data.remote.response.deleteleave

import java.io.Serializable

data class DeleteLeaveResponse(
    val statusCode:Int,
    val body:DeletLeaveBody
):Serializable
