package com.empcloud.empmonitor.data.remote.response.create_task

import java.io.Serializable

data class CreateTaskResponse(

    val statusCode:Int,
    val body:CreateTaskBody
):Serializable
