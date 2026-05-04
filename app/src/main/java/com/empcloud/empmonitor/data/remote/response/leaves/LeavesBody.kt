package com.empcloud.empmonitor.data.remote.response.leaves

import java.io.Serializable

data class  LeavesBody(
    val status:String,
    val message:String,
    val data:List<LeavesList>

    ):Serializable
