package com.empcloud.empmonitor.data.remote.response.clientfetch

import java.io.Serializable

data class ClientFetchResponse(
    val statusCode:Int,
    val body:ClientFetchBody
):Serializable
