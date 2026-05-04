package com.empcloud.empmonitor.data.remote.response.update_task

import java.io.Serializable

data class UpdateTaskBody(
    val status:String,
    val message:String
):Serializable
