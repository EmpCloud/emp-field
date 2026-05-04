package com.empcloud.empmonitor.data.remote.request.update_task

import com.empcloud.empmonitor.data.remote.request.create_task.AmountCurrency
import com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl
import com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl
import java.io.Serializable

data class UpdateTaskModel(
    val taskId:String,
    val status:Int,
    val currentDateTime:String,
    val latitude:String,
    val longitude:String,
    val value:AmountCurrency?,
    val taskVolume:Double,
    val tagLogs:List<Tags>?,
    val files:MutableList<FilesUrl>,
    val images:MutableList<ImgaesUrl>,
):Serializable
