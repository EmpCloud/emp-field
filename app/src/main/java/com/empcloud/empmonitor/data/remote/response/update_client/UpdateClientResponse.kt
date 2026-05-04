package com.empcloud.empmonitor.data.remote.response.update_client

import java.io.Serializable

data class UpdateClientResponse(
    val statusCode:Int,
    val body:BodyUpdateClient
):Serializable

