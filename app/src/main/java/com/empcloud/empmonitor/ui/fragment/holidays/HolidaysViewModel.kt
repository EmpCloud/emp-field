package com.empcloud.empmonitor.ui.fragment.holidays

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.response.holidays.HolidaysResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HolidaysViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val holidayDataFlow = Channel<ApiState<HolidaysResponse>> (Channel.BUFFERED)
    val observeHolidayData = holidayDataFlow.receiveAsFlow()

    fun invokeHolidayFetch(authToken:String){
        viewModelScope.launch {
            repository.getHolidays(authToken).collect{response ->
                holidayDataFlow.send(response)
            }
        }
    }
}