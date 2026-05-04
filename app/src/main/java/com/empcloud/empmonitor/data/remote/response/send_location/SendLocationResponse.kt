package com.empcloud.empmonitor.data.remote.response.send_location

import java.io.Serializable

data class SendLocationResponse(
    val statusCode:Int,
    val body:ResponseBodySendLocation
):Serializable
