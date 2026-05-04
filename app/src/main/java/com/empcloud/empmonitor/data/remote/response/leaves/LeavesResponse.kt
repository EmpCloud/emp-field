package com.empcloud.empmonitor.data.remote.response.leaves

import java.io.Serializable

data class LeavesResponse(
    val statusCode:Int,
    val body:LeavesBody
):Serializable
