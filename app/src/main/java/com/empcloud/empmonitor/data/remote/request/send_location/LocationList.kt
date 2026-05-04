package com.empcloud.empmonitor.data.remote.request.send_location

import java.io.Serializable

data class LocationList(

    val date:String,
    val time:String,
    val latitude:Double,
    val longitude:Double
):Serializable
