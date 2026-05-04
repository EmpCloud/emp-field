package com.empcloud.empmonitor.data.remote.response.update_task

import java.io.Serializable

data class UpdateTaskResponse(
    val statusCode:Int,
    val body:UpdateTaskBody
):Serializable
