package com.empcloud.empmonitor.data.remote.response.fetch_task

import java.io.Serializable

data class FetchTaskResponse(
    val statusCode:Int,
    val body:FetchTaskResponseBody
):Serializable
