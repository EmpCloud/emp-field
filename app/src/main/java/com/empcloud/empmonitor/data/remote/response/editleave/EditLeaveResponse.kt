package com.empcloud.empmonitor.data.remote.response.editleave

import com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveBody
import java.io.Serializable

data class EditLeaveResponse(
    val statusCode:Int,
    val body: EditLeaveBody
):Serializable
