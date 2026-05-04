package com.empcloud.empmonitor.data.remote.request.edit_attendanc

import java.io.Serializable

data class EditAttendanceModel(
    val date:String,
    val check_in:String,
    val check_out:String,
    val reason:String

    ):Serializable
