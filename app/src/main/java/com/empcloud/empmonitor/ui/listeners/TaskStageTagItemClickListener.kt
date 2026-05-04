package com.empcloud.empmonitor.ui.listeners

import com.empcloud.empmonitor.data.remote.response.task_stage_selection.Data

interface TaskStageTagItemClickListener {

    fun onItemClickTags(position : Int,dataTag : Data)
}