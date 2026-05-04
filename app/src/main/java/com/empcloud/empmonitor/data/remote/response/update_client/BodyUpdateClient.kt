package com.empcloud.empmonitor.data.remote.response.update_client

import java.io.Serializable

data class BodyUpdateClient(
    val status:String,
    val message:String
): Serializable
