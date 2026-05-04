package com.empcloud.empmonitor.data.remote.response.url_response_pics

import java.io.Serializable

data class UrlPicsResponse(
    val code:Int,
    val data:ResponseUrl,
    val error:List<Error>
):Serializable
