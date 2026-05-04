package com.empcloud.empmonitor.ui.activity.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel
import com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel@Inject constructor(private val repository: NetworkRepository) : ViewModel() {

    private val resetPasswordDataFlow = Channel<ApiState<ResetPasswordResponse>> (Channel.BUFFERED)
    val observerResetPassData = resetPasswordDataFlow.receiveAsFlow()

    fun invokeResetPassCall(resetPasswordModel: ResetPasswordModel){

        viewModelScope.launch {
            repository.getResetData(resetPasswordModel).collect{ response->
                resetPasswordDataFlow.send(response)
            }
        }
    }
}