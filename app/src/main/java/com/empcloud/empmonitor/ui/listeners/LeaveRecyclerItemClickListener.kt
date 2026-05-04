package com.empcloud.empmonitor.ui.listeners

import com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesList

interface LeaveRecyclerItemClickListener {

//    fun onEditClick(item: MyItem)
    fun onItemClicked(leave:LeavesList,position:Int)
}