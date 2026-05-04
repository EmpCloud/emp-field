package com.empcloud.empmonitor.data.remote.response.filter_task

import java.io.Serializable

data class FilterFetchTaskDetail(
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
    val taskApproveStatus:Int,
    val taskDescription:String,
    val task_image_url:String,
    val form_url:String,
    val empStartTime:String,
    val empEndTime:String,
    val clientName:String
):Serializable
