package com.empcloud.empmonitor.data.remote.request.send_location

import java.io.Serializable

data class LocationList(

    val date:String,
    val time:String,
    val latitude:Double,
    val longitude:Double,
    val batteryPercent: Int? = null,
    val isCharging: Boolean? = null
):Serializable
