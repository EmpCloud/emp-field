package com.empcloud.empmonitor.data.remote.response.createclient

import java.io.Serializable

data class CreateClientBody(
    val status:String,
    val message:String,
    val data:CreateClientData
):Serializable
