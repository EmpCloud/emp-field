package com.empcloud.empmonitor.data.remote.response.update_reschedue

import java.io.Serializable

data class UpdateResceduleResponse(

    val statusCode:Int,
    val body:UpdateResceduleBody
):Serializable

