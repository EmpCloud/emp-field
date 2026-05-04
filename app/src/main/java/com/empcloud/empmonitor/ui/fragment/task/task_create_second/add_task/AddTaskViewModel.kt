package com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_task

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.create_task.CreateTaskModel
import com.empcloud.empmonitor.data.remote.request.create_task.FilesUrl
import com.empcloud.empmonitor.data.remote.request.create_task.ImgaesUrl
import com.empcloud.empmonitor.data.remote.request.update_reschedule.UpdateRescheduleModel
import com.empcloud.empmonitor.data.remote.request.update_task.UpdateTaskModel
import com.empcloud.empmonitor.data.remote.response.create_task.CreateTaskResponse
import com.empcloud.empmonitor.data.remote.response.update_reschedue.UpdateResceduleResponse
import com.empcloud.empmonitor.data.remote.response.update_task.UpdateTaskResponse
import com.empcloud.empmonitor.data.remote.response.url_response_pics.UrlPicsResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {


    private val createTaskDataCall = Channel<ApiState<CreateTaskResponse>>(Channel.BUFFERED)
    val observeCreateTaskCall = createTaskDataCall.receiveAsFlow()

    fun invokeCreateTask(accesToken:String,createTaskModel: CreateTaskModel){
        viewModelScope.launch {
            repository.createTaskCall(accesToken,createTaskModel).collect{response ->
                createTaskDataCall.send(response)
            }
        }
    }


    private val sendPicsDataFlow = Channel<ApiState<UrlPicsResponse>>(Channel.BUFFERED)
    val observeSendPicsDataCall = sendPicsDataFlow.receiveAsFlow()

    fun invokeSendPicsCall(accesToken: String,files: MultipartBody.Part){
        viewModelScope.launch {
            repository.sendPicsToApi(accesToken,files).collect{response ->
                sendPicsDataFlow.send(response)
            }
        }
    }


}