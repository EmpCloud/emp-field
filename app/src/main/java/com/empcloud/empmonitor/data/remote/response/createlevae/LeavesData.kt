package com.empcloud.empmonitor.data.remote.response.createlevae

import java.io.Serializable

data class LeavesData(
    val leave_id:Int,
    val number_of_days:Float,
    val timezone:String

    ):Serializable
