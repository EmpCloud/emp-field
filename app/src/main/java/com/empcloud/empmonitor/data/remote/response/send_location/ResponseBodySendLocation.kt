package com.empcloud.empmonitor.data.remote.response.send_location

import java.io.Serializable

data class ResponseBodySendLocation(
    val status:String,
    val message:String,
    val data:FrequencyResponse
):Serializable
