package com.empcloud.empmonitor.ui.fragment.leaves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.request.LEAVES.LeavesRequestModel
import com.empcloud.empmonitor.data.remote.request.createleave.CreateLeaveRequestModel
import com.empcloud.empmonitor.data.remote.request.delete.DeleteLeaveRequest
import com.empcloud.empmonitor.data.remote.request.editleave.EditLeaveRequestModel
import com.empcloud.empmonitor.data.remote.response.createlevae.CreateLeaveResponse
import com.empcloud.empmonitor.data.remote.response.deleteleave.DeleteLeaveResponse
import com.empcloud.empmonitor.data.remote.response.editleave.EditLeaveResponse
import com.empcloud.empmonitor.data.remote.response.get_leave_type.GetLeaveTypeResponse
import com.empcloud.empmonitor.data.remote.response.leaves.LeavesResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeavesViewModel @Inject constructor(val repository: NetworkRepository):ViewModel(){

    private val leavesDataFlow = Channel<ApiState<LeavesResponse>> (Channel.BUFFERED)
    val observeLeavesFlow = leavesDataFlow.receiveAsFlow()

    fun invokeLeaves(authToken:String,leavesRequestModel: LeavesRequestModel){
        viewModelScope.launch {
            repository.getLeaves(authToken,leavesRequestModel).collect{response ->
                leavesDataFlow.send(response)
            }
        }
    }

    private val createLeaveFlow = Channel<ApiState<CreateLeaveResponse>>(Channel.BUFFERED)
    val observeCreateLeaveFlow = createLeaveFlow.receiveAsFlow()

    fun invokeCreateLeave(authToken: String,createLeaveRequestModel: CreateLeaveRequestModel){

        viewModelScope.launch {
            repository.createLeaveCall(authToken,createLeaveRequestModel).collect{response ->
                createLeaveFlow.send(response)
            }
        }
    }

    private val editLeaveFlow = Channel<ApiState<EditLeaveResponse>>(Channel.BUFFERED)
    val observeEditLeaveCall = editLeaveFlow.receiveAsFlow()

    fun invokeEditLeave(accesToken:String,editLeaveRequestModel: EditLeaveRequestModel){

        viewModelScope.launch {
            repository.editLeaveCall(accesToken,editLeaveRequestModel).collect{response ->
                editLeaveFlow.send(response)
            }
        }
    }

    private val deleteLeaveFlow = Channel<ApiState<DeleteLeaveResponse>>(Channel.BUFFERED)
    val observedeleteLeaveCall = deleteLeaveFlow.receiveAsFlow()

    fun invokeDeleteLeave(accesToken:String,deleteLeaveRequest: DeleteLeaveRequest){

        viewModelScope.launch {
            repository.deleteLeaveCall(accesToken,deleteLeaveRequest).collect{response ->
                deleteLeaveFlow.send(response)
            }
        }
    }

    private val getleaveTypeFlow = Channel<ApiState<GetLeaveTypeResponse>>(Channel.BUFFERED)
    val observeGetLeaveType = getleaveTypeFlow.receiveAsFlow()

    fun invokeGetLeaveType(accesToken:String){

        viewModelScope.launch {
            repository.getLeaveType(accesToken).collect{response ->
                getleaveTypeFlow.send(response)
            }
        }
    }
}