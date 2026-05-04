package com.empcloud.empmonitor.data.remote.response.create_task

import java.io.Serializable

data class CreateTaskDetail(

    val clientId:String,
    val orgId:String,
    val taskName:String,
    val emp_id:String,
    val start_time:String,
    val end_time:String,
    val form_url:String,
    val task_image_url:String,
    val taskDescription:String,
    val taskApproveStatus:String,
    val empStartTime:String,
    val empEndTime:String,
    val deletedStatus:String,
    val _id:String,
    val createdAt:String,
    val updatedAt:String,


    ):Serializable
