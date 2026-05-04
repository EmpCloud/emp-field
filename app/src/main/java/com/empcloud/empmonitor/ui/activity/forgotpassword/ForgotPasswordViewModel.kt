package com.empcloud.empmonitor.ui.activity.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.forgotpassword.ForgotPasswordDataModel
import com.empcloud.empmonitor.data.remote.response.otpresponse.OtpResponseModel
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val forgotDataFlow = Channel<ApiState<OtpResponseModel>> (Channel.BUFFERED)
    val observerOTP = forgotDataFlow.receiveAsFlow()
    fun invokeOtpCall(forgotPasswordDataModel: ForgotPasswordDataModel){
        viewModelScope.launch {
            repository.getOtp(forgotPasswordDataModel).collect{response ->
                forgotDataFlow.send(response)

            }
        }
    }
}