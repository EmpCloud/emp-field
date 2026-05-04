package com.empcloud.empmonitor.data.remote.response.editleave

import com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeave
import java.io.Serializable

data class EditLeaveData(
    val code:Int?,
    val message:String?,
    val error:String?,
    val data: EditLeave
):Serializable
