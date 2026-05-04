package com.empcloud.empmonitor.data.remote.response.fetch_task

import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency
import com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl
import com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl
import com.empcloud.empmonitor.data.remote.request.update_task.Tags
import java.io.Serializable

data class FetchTaskDetail(
    val value:AmountCurrency,
    val _id:String,
    val taskName:String,
    val orgId:String,
    val emp_id:String,
    val clientId:String,
    val start_time:String,
    val end_time:String,
    val address1:String,
    val address2:String,
    val city:String,
    val latitude:String,
    val longitude:String,
    val createdAt:String,
    val updatedAt:String,
    val deletedStatus:String,
    var taskApproveStatus:Int,
    val taskDescription:String,
    val task_image_url:String,
    val form_url:String,
    val empStartTime:String,
    val empEndTime:String,
    val clientName:String,
    val files:MutableList<FilesUrl>,
    val images:MutableList<ImgaesUrl>,
    val taskVolume:Double,
    val tagLogs:List<Tags>?,
    val date:String


):Serializable
