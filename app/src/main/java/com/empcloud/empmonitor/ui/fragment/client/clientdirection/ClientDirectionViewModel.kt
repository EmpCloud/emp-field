package com.empcloud.empmonitor.ui.fragment.client.clientdirection

import androidx.lifecycle.ViewModel
import com.empcloud.empmonitor.network.maps_service.MapApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClientDirectionViewModel @Inject constructor(val mapApiService: MapApiService):ViewModel() {

    val mMapApiService = mapApiService
}