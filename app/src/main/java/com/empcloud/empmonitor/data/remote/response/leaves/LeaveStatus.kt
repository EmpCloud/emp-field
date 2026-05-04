package com.empcloud.empmonitor.data.remote.response.leaves

import java.io.Serializable

data class LeaveStatus(

    val pending_leaves:Int,
    val approved_leaves:Int,
    val rejected_leaves:Int

    ):Serializable
