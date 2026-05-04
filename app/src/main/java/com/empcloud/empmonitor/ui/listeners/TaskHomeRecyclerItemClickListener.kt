package com.empcloud.empmonitor.ui.listeners

import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskDetail

interface TaskHomeRecyclerItemClickListener {

    fun onItemClickTask(position:Int,fetchTaskDetail: FetchTaskDetail,tag:Int,status:Int)
}