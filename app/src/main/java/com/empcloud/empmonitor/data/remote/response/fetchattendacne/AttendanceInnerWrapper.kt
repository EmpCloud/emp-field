package com.empcloud.empmonitor.data.remote.response.fetchattendacne

import java.io.Serializable

data class AttendanceInnerWrapper(
    val check_in: String?,
    val check_out: String?
) : Serializable