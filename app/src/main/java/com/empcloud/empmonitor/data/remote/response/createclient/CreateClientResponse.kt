package com.empcloud.empmonitor.data.remote.response.createclient

import java.io.Serializable

data class CreateClientResponse(
    val statusCode:Int,
    val body:CreateClientBody
):Serializable
