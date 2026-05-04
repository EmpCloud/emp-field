package com.empcloud.empmonitor.ui.fragment.task.task_first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.filter_task.FilteerTaskModel
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel
import com.empcloud.empmonitor.data.remote.response.fetch_task.FetchTaskResponse
import com.empcloud.empmonitor.data.remote.response.filter_task.FilterTaskResponse
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
class TaskHomeViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val fetchTaskDataFlow = Channel<ApiState<FetchTaskResponse>>(Channel.BUFFERED)
    val observeFetchTaskCall = fetchTaskDataFlow.receiveAsFlow()

    fun invokeFetchTask(accessToken:String){
        viewModelScope.launch {
            repository.fetchTaskCall(accessToken).collect{response ->
                fetchTaskDataFlow.send(response)
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

    private val filterTaskFlow = Channel<ApiState<FetchTaskResponse>>(Channel.BUFFERED)
    val observeFilterTaskCall = filterTaskFlow.receiveAsFlow()

    fun invokeFilterTask(accessToken: String,filteerTaskModel: FilteerTaskModel){
        viewModelScope.launch {
            repository.filterTaskCall(accessToken,filteerTaskModel).collect{response ->
                filterTaskFlow.send(response)
            }
        }
    }

    private val updateReschedleTaskDataFlow = Channel<ApiState<UpdateResceduleResponse>>(Channel.BUFFERED)
    val observeUpdateRescheduleTaskFlow = updateReschedleTaskDataFlow.receiveAsFlow()

    fun invokeRescheduleTaskCall(accessToken: String,updateRescheduleModel: UpdateRescheduleModel){
        viewModelScope.launch {
            repository.updateRescheduleCall(accessToken,updateRescheduleModel).collect{response ->
                updateReschedleTaskDataFlow.send(response)
            }
        }
    }
}