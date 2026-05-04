package com.empcloud.empmonitor.data.remote.request.create_task

import java.io.Serializable

data class CreateTaskModel(
    val clientId:String,
    val taskName:String,
    val start_time:String,
    val end_time:String,
    val form_url:String?,
    val task_image_url:String?,
    val taskDescription:String,
    val files:MutableList<FilesUrl>,
    val images:MutableList<ImgaesUrl>,
    val value:AmountCurrency?,
    val taskVolume:Double?,
    val date:String
    ):Serializable
