package com.empcloud.empmonitor.data.remote.response.createlevae

import com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveData
import java.io.Serializable

data class CreateLeaveBody(
    val status:String,
    val message:String,
    val data:CreateLeaveData,
    val error: EditLeaveData
):Serializable
