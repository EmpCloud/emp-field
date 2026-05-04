package com.empcloud.empmonitor.ui.activity.mobilelogin.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel
import com.empcloud.empmonitor.data.remote.request.verifymobile.VerifyMobileModel
import com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse
import com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileLoginResponse
import com.empcloud.empmonitor.data.remote.response.mobilelogin.MobileLoginResponseBody
import com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse
import com.empcloud.empmonitor.data.remote.response.tracking_settings.TrackingSettingsResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MobileVerifiactionViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val mobileVerifyModel = Channel<ApiState<MobileLoginResponse>> (Channel.BUFFERED)
    val observerMobileData = mobileVerifyModel.receiveAsFlow()

    fun invokeVerifyCall(verifyMobileModel: VerifyMobileModel){

        viewModelScope.launch {
            repository.getVerifyData(verifyMobileModel).collect{ response->
                mobileVerifyModel.send(response)
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