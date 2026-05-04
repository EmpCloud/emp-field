package com.empcloud.empmonitor.data.remote.response.editleave

import java.io.Serializable

data class EditLeavesData(
    val leave_id:Int,
    val number_of_days:Double,
):Serializable
