package com.empcloud.empmonitor.data.remote.request.filter_task

import java.io.Serializable

data class FilteerTaskModel(
    val date:String,
    val status:Int
):Serializable
