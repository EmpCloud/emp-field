package com.empcloud.empmonitor.ui.fragment.client.clienthome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empcloud.empmonitor.data.remote.response.clientfetch.ClientFetchResponse
import com.empcloud.empmonitor.network.NetworkRepository
import com.empcloud.empmonitor.network.api_satatemanagement.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientHomeViewModel @Inject constructor(val repository: NetworkRepository):ViewModel() {

    private val clientFetchFlow = Channel<ApiState<ClientFetchResponse>>(Channel.BUFFERED)
    val observeClientFetch = clientFetchFlow.receiveAsFlow()

    fun invokeFetchClient(accessToken:String){

        viewModelScope.launch {
            repository.fetchClientCall(accessToken).collect{response ->
                clientFetchFlow.send(response)
            }
        }
    }
}