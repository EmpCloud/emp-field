package com.empcloud.empmonitor.data.remote.response.filter_task

import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponseBody
import java.io.Serializable

data class FilterTaskResponse(
    val statusCode:Int,
    val body: FilterTaskResponseBody
):Serializable
