package com.empcloud.empmonitor.ui.activity.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.login.LoginDataModel
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.data.remote.request.verify_email.VerifyEmailModel
import com.empcloud.empmonitor.data.remote.response.login.LoginResponse
import com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse
import com.empcloud.empmonitor.data.remote.response.verify_email.VerifyEmailResponse
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import com.empcloud.empmonitor.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: NetworkRepository) : ViewModel() {

    private val loginDataFlow = Channel<ApiState<LoginResponse>> (Channel.BUFFERED)
    val observerLoginData = loginDataFlow.receiveAsFlow()


    fun invokeLoginCall(loginDataModel: LoginDataModel){

//        Log.d("Apicode","Inside ViewModel")
        viewModelScope.launch {
            repository.getLoginData(loginDataModel).collect{ response->
                loginDataFlow.send(response)
            }
        }
    }


    private val verifyEmailFlow = Channel<ApiState<VerifyEmailResponse>> (Channel.BUFFERED)
    val observerEmailData = verifyEmailFlow.receiveAsFlow()


    fun invokeVerifyEmail(verifyEmailModel: VerifyEmailModel){

//        Log.d("Apicode","Inside ViewModel")
        viewModelScope.launch {
            repository.getEmailVerify(verifyEmailModel).collect{ response->
                verifyEmailFlow.send(response)
            }
        }
    }

    private val trackingSettingsFlow = Channel<ApiState<TrackingSettingsResponse>>(Channel.BUFFERED)
    val observeTrackingSettings = trackingSettingsFlow.receiveAsFlow()

    fun invokeTrackingSettings(accessToken: String) {
        viewModelScope.launch {
            repository.getTrackingSettings(accessToken).collect { response ->
                trackingSettingsFlow.send(response)
            }
        }
    }

    private val markAttendanceFlow = Channel<ApiState<MarkAttendanceResponse>>(Channel.BUFFERED)
    val observeMarkAttendance = markAttendanceFlow.receiveAsFlow()

    fun invokeMarkAttendance(accessToken: String, markAttendanceModel: MarkAttendanceModel) {
        viewModelScope.launch {
            repository.getMarkAttendanceData(accessToken, markAttendanceModel).collect { response ->
                markAttendanceFlow.send(response)
            }
        }
    }

}