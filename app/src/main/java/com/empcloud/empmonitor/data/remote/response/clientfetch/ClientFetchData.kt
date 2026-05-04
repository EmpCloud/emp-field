package com.empcloud.empmonitor.data.remote.response.clientfetch

import java.io.Serializable

data class ClientFetchData(
    val clientDetail:List<ClientFetchDetail>
):Serializable
