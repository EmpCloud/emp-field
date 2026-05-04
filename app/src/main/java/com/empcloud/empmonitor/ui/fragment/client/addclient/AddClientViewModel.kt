package com.empcloud.empmonitor.ui.fragment.client.addclient

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.createclient.CreateClientModel
import com.empcloud.empmonitor.data.remote.response.createclient.CreateClientResponse
import com.empcloud.empmonitor.data.remote.response.uploadprofile.UploadProfileResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class AddClientViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val createClientFlow = Channel<ApiState<CreateClientResponse>>(Channel.BUFFERED)
    val observeCreateClientCall = createClientFlow.receiveAsFlow()

    fun invokeCreateClient(authToken:String,createClientModel: CreateClientModel){

        viewModelScope.launch {
            repository.createClientCall(authToken,createClientModel).collect{response ->
                createClientFlow.send(response)
            }
        }
    }

    private val profileImgFlow = Channel<ApiState<UploadProfileResponse>> (Channel.BUFFERED)
    val observeImageData = profileImgFlow.receiveAsFlow()

    fun invokeUpdateImage(authToken:String,files: MultipartBody.Part){

        viewModelScope.launch {

            repository.getClinetUploadProfile(authToken,files).collect{response ->
                profileImgFlow.send(response)
//                Log.d("ResponseViewModel",response.toString())
            }
        }
    }
}