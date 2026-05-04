package com.empcloud.empmonitor.ui.listeners

import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceFullData

interface AttendacneRecyclerItemClickListener {

    fun onItemClicked(position:Int, attendanceFullData: AttendanceFullData)

}