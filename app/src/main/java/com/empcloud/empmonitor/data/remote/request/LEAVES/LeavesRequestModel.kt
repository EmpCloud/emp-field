package com.empcloud.empmonitor.data.remote.request.LEAVES

import java.io.Serializable

data class LeavesRequestModel(
    val startDate:String,
    val endDate:String

    ):Serializable
