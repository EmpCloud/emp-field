package com.empcloud.empmonitor.ui.fragment.edit_client.first

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.update_client.UpdatecClientModel
import com.empcloud.empmonitor.data.remote.response.update_client.UpdateClientResponse
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
class EditClientViewModel @Inject constructor(val repository: NetworkRepository):ViewModel(){

    private val updateClinetDataFlow = Channel<ApiState<UpdateClientResponse>>(Channel.BUFFERED)
    val observeUpdateClinetFlow = updateClinetDataFlow.receiveAsFlow()

    fun invokeUpdateClientCall(accessToken: String, clientId: String, updatecClientModel: UpdatecClientModel) {
        viewModelScope.launch {

            repository.updateCLientCall(accessToken,clientId,updatecClientModel).collect{response ->
                updateClinetDataFlow.send(response)
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