package com.empcloud.empmonitor.data.remote.response.notification

data class Data(
    val previousTasks: List<PreviousTask>,
    val rescheduledTasks: List<RescheduledTask>
)