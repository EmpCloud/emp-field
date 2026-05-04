package com.empcloud.empmonitor.data.remote.response.fetch_task

import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import java.io.Serializable

data class FetchTaskResponseBody(
    val status:String,
    val message:String,
    val data:List<FetchTaskDetail>
):Serializable
