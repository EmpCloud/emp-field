package com.empcloud.empmonitor.data.remote.response.holidays

import java.io.Serializable

data class HolidayList(
    val id :String,
    val holiday_name :String,
    val holiday_date :String,
    val organization_id :Int,
    val created_at :String,
    val updated_at :String,
    ):Serializable
