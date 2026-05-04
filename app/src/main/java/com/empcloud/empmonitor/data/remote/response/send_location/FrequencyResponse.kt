package com.empcloud.empmonitor.data.remote.response.send_location

import java.io.Serializable

data class FrequencyResponse(

    val currentFrequency:Int,
    val currentRadius:Int
):Serializable
