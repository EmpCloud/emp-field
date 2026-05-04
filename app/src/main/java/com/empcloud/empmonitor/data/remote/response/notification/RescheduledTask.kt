package com.empcloud.empmonitor.data.remote.response.notification

data class RescheduledTask(
    val __v: Int,
    val _id: String,
    val clientId: String,
    val createdAt: String,
    val date: String,
    val empEndTime: Any,
    val empStartTime: String,
    val emp_id: String,
    val end_time: String,
    val files: List<Any>,
    val images: List<Any>,
    val orgId: String,
    val start_time: String,
    val tagId: Any,
    val taskApproveStatus: Int,
    val taskDescription: String,
    val taskName: String,
    val taskVolume: String,
    val updatedAt: String,
    val value: Value
)