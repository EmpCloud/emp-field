package com.empcloud.empmonitor.data.remote.response.clientfetch

import java.io.Serializable

data class ClientFetchBody(
    val status:String,
    val message:String,
    val data:List<ClientFetchDetail>
):Serializable
