package com.empcloud.empmonitor.data.remote.response.mode_of_transport

import java.io.Serializable

data class ModeTransportResponse(

    val statusCode:Int,
    val body:TransportSelctionBody
):Serializable
