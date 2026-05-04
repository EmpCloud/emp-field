package com.empcloud.empmonitor.data.remote.response.task_stage_selection

data class Body(
    val data: List<Data>,
    val message: String,
    val status: String
)