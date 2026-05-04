package com.empcloud.empmonitor.data.remote.request.create_task

import java.io.Serializable

data class RecurrenceData(
    val startDate:String?,
    val endDate:String?,
    val excludedDays:List<Any> = emptyList<Any>(),
    )
