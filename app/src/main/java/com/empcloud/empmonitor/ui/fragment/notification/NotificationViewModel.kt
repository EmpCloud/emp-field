package com.empcloud.empmonitor.ui.fragment.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.response.notification.NotificationResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val notificationDataFlow = Channel<ApiState<NotificationResponse>>(Channel.BUFFERED)
    val observenNotificationFlow = notificationDataFlow.receiveAsFlow()

    fun invokeGetNotification(accessToken:String){
        viewModelScope.launch {
            repository.getNotification(accessToken).collect{response ->
                notificationDataFlow.send(response)
            }
        }
    }
}