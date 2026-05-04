package com.empcloud.empmonitor.data.remote.response.createlevae

import java.io.Serializable

data class CreateLeaveData(
    val code:Int,
    val message:String,
    val error:String,
    val data:CreateLeave

    ):Serializable
