package com.empcloud.empmonitor.data.remote.response.send_location

import java.io.Serializable

data class ResponseBodySendLocation(
    val status:String? = null,
    val message:String? = null,
    val data:FrequencyResponse? = null
):Serializable
