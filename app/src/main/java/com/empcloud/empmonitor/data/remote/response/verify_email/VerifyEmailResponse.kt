package com.empcloud.empmonitor.data.remote.response.verify_email

import VerifyEmailBody
import java.io.Serializable

data class VerifyEmailResponse(

    val statusCode:Int,
    val body:VerifyEmailBody
):Serializable

