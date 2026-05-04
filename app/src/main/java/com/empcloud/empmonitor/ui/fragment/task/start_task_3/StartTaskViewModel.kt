package com.empcloud.empmonitor.ui.fragment.task.start_task_3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel
import com.empcloud.empmonitor.data.remote.response.task_stage_selection.TaskStageSelectionResponse
import com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse
import com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartTaskViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val updateReschedleTaskDataFlow = Channel<ApiState<UpdateResceduleResponse>>(Channel.BUFFERED)
    val observeUpdateRescheduleTaskFlow = updateReschedleTaskDataFlow.receiveAsFlow()

    fun invokeRescheduleTaskCall(accessToken: String,updateRescheduleModel: UpdateRescheduleModel){
        viewModelScope.launch {
            repository.updateRescheduleCall(accessToken,updateRescheduleModel).collect{response ->
                updateReschedleTaskDataFlow.send(response)
            }
        }
    }

    private val updateTaskDataFlow = Channel<ApiState<UpdateTaskResponse>>(Channel.BUFFERED)
    val observeUpdateTaskFlow = updateTaskDataFlow.receiveAsFlow()

    fun invokeUpdateTaskCall(accessToken: String,updateTaskModel: UpdateTaskModel){
        viewModelScope.launch {
            repository.updateTaskCall(accessToken,updateTaskModel).collect{response ->
                updateTaskDataFlow.send(response)
            }
        }
    }

    private val taskStageTagsFlow = Channel<ApiState<TaskStageSelectionResponse>> (Channel.BUFFERED)
    val observeTagsFlow = taskStageTagsFlow.receiveAsFlow()

    fun invokeGetTagsStage(accessToken: String){
        viewModelScope.launch {
            repository.getTaskStageTags(accessToken).collect{response ->
                taskStageTagsFlow.send(response)
            }
        }
    }
}