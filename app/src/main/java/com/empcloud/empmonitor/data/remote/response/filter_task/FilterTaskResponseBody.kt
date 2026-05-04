package com.empcloud.empmonitor.data.remote.response.filter_task

import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail
import java.io.Serializable

data class FilterTaskResponseBody(
    val status:String,
    val message:String,
    val data:List<FilterFetchTaskDetail>
):Serializable
