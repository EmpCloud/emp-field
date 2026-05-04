package com.empcloud.empmonitor.ui.fragment.task.task_create_second.add_picture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AddPictureViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

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