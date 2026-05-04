package com.empcloud.empmonitor.data.remote.response.editleave

import com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveData
import java.io.Serializable

data class EditLeaveBody(
    val status:String,
    val message:String,
    val data: EditLeaveData,
    val error: EditLeaveData

):Serializable
