package com.empcloud.empmonitor.data.remote.request.update_reschedule

import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency
import com.empcloud.empmonitor.data.remote.request.update_task.Tags
import java.io.Serializable

data class UpdateRescheduleModel(

    val taskId:String,
    val clientId:String,
    val taskName:String,
    val start_time:String,
    val end_time:String,
    val taskDescription:String,
    val date:String,
    val value:AmountCurrency?,
    val taskVolume:Double?,
    val tagLogs:List<Tags>?
):Serializable

