package com.empcloud.empmonitor.data.remote.response.deleteleave

import java.io.Serializable

data class DeletLeaveBody(
    val status:String,
    val message:String,
    val data:DelteData

    ):Serializable
