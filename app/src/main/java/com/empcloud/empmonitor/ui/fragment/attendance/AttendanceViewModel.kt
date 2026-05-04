package com.empcloud.empmonitor.ui.fragment.attendance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.attendance.AttendanceRequestModel
import com.empcloud.empmonitor.data.remote.request.edit_attendanc.EditAttendanceModel
import com.empcloud.empmonitor.data.remote.response.attendance.AttendanceResponse
import com.empcloud.empmonitor.data.remote.response.edit_attendance.EditAttendanceResponse
import com.empcloud.empmonitor.data.remote.response.holidays.HolidaysResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(val repository: NetworkRepository):ViewModel(){


    private val attendanceDataFlow = Channel<ApiState<AttendanceResponse>> (Channel.BUFFERED)
    val observeAttendaceFlow = attendanceDataFlow.receiveAsFlow()

    fun invokeAttendanceCall(authToken:String,attendanceRequestModel: AttendanceRequestModel){
//        Log.d("attendace","inside")
        viewModelScope.launch {
            repository.getAttendance(authToken,attendanceRequestModel).collect{response ->
                attendanceDataFlow.send(response)
//                Log.d("attendace",response.toString())
            }
        }
    }

    private val editAttendanceDataFlow = Channel<ApiState<EditAttendanceResponse>>(Channel.BUFFERED)
    val observeEditAttendanceFlow = editAttendanceDataFlow.receiveAsFlow()

    fun invokeEditAttendaceCall(accessToken:String,editAttendanceModel: EditAttendanceModel){

        viewModelScope.launch {
            repository.fetchEditAttendanceCall(accessToken,editAttendanceModel).collect{response ->
                editAttendanceDataFlow.send(response)
            }
        }

    }
}