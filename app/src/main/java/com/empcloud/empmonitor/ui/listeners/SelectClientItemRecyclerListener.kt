package com.empcloud.empmonitor.ui.listeners

import com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskDetail
import com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskResponse

interface SelectClientItemRecyclerListener {

    fun onItemClick(position:Int,createTaskDetail:CreateTaskDetail)
}