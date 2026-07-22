package com.empcloud.empmonitor.data.remote.response.send_location

import java.io.Serializable

data class SendLocationResponse(
    val statusCode:Int? = null,
    val body:ResponseBodySendLocation? = null,
    val status:String? = null,
    val message:String? = null,
    val data:FrequencyResponse? = null
):Serializable {
    val resolvedStatusCode: Int?
        get() = statusCode

    val resolvedMessage: String?
        get() = body?.message ?: message

    val resolvedData: FrequencyResponse?
        get() = body?.data ?: data

    fun isSuccessfulPayload(): Boolean {
        return statusCode == null || statusCode == 200
    }
}
