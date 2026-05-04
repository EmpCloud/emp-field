package com.empcloud.empmonitor.ui.fragment.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel
import com.empcloud.empmonitor.data.remote.request.mark_attendance.MarkAttendanceModel
import com.empcloud.empmonitor.data.remote.request.mode_of_transport.ModeOFTransportModel
import com.empcloud.empmonitor.data.remote.request.resetpassword.ResetPasswordModel
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceResponse
import com.empcloud.empmonitor.data.remote.response.fetchattendacne.FetchAttendanceResponse
import com.empcloud.empmonitor.data.remote.response.markattendance.MarkAttendanceResponse
import com.empcloud.empmonitor.data.remote.response.mode_of_transport.ModeTransportResponse
import com.empcloud.empmonitor.data.remote.response.resetpassword.ResetPasswordResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: NetworkRepository):ViewModel(){

    private val markAttendanceFlow = Channel<ApiState<MarkAttendanceResponse>> (Channel.BUFFERED)
    val observerAttendanceFlow = markAttendanceFlow.receiveAsFlow()

    fun invokeAttendaceMarking(authToken:String,markAttendanceModel: MarkAttendanceModel){

        viewModelScope.launch {
            repository.getMarkAttendanceData(authToken,markAttendanceModel).collect{ response->
                markAttendanceFlow.send(response)
            }
        }
    }

    private val fetchAttendanceFlow = Channel<ApiState<FetchAttendanceResponse>>(Channel.BUFFERED)
    val observeFetchAttendanceCall = fetchAttendanceFlow.receiveAsFlow()

    fun invokeFetchAttendance(authToken: String){
        viewModelScope.launch {
            repository.getAttendacenData(authToken).collect{response ->
                fetchAttendanceFlow.send(response)
            }
        }
    }

    private val transportUpdationFlow = Channel<ApiState<ModeTransportResponse>> (Channel.BUFFERED)
    val observeTransportCall = transportUpdationFlow.receiveAsFlow()

    fun invokeTransportUpdation(authToken:String,modeOFTransportModel: ModeOFTransportModel){
        Log.d("attendace","inside")
        viewModelScope.launch {
            repository.sendModeSelection(authToken,modeOFTransportModel).collect{response ->
                transportUpdationFlow.send(response)
//                Log.d("attendace",response.toString())
            }
        }
    }
}