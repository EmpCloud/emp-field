package com.empcloud.empmonitor.ui.fragment.update_profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.updateprofile.UpdateProfileModel
import com.empcloud.empmonitor.data.remote.response.fetchprofile.FetchProfileResponse
import com.empcloud.empmonitor.data.remote.response.uploadprofile.UpdateProfileResponse
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
class UpdateProfileViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val profileDataFlow = Channel<ApiState<FetchProfileResponse>> (Channel.BUFFERED)
    val observerProfileData = profileDataFlow.receiveAsFlow()

    fun invokeFetchProfileCall(authToken:String){

        viewModelScope.launch {
            repository.getFetchProfile(authToken).collect{ response->
                profileDataFlow.send(response)
            }
        }
    }

    private val updateDataFlow = Channel<ApiState<UpdateProfileResponse>> (Channel.BUFFERED)
    val observerUpdataData = updateDataFlow.receiveAsFlow()

    fun invokeUpdataDataCall(authToken:String,updateProfileModel: UpdateProfileModel){

        viewModelScope.launch {

            repository.getUpdateProfile(authToken,updateProfileModel).collect{response ->
                updateDataFlow.send(response)
            }
        }
    }

    private val profileImgFlow = Channel<ApiState<UploadProfileResponse>> (Channel.BUFFERED)
    val observeImageData = profileImgFlow.receiveAsFlow()

    fun invokeUpdateImage(authToken:String,files: MultipartBody.Part){

        viewModelScope.launch {

            repository.getUploadProfile(authToken,files).collect{response ->
                profileImgFlow.send(response)
//                Log.d("ResponseViewModel",response.toString())
            }
        }
    }

    fun uploadData(accesToken: String, updateProfileModel: UpdateProfileModel){

        invokeUpdataDataCall(accesToken,updateProfileModel)
    }
}