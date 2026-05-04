package com.empcloud.empmonitor.data.remote.response.fetchattendacne

import java.io.Serializable

data class AttendanceWrapper(
    val data: AttendanceInnerWrapper
) : Serializable