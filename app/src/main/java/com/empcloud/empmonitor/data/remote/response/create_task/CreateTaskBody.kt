package com.empcloud.empmonitor.data.remote.response.create_task

import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchDetail
import java.io.Serializable

data class CreateTaskBody(

    val status:String,
    val message:String,
    val data:CreateTaskDetail
):Serializable
