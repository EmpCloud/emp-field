package com.empcloud.empmonitor.data.remote.request.create_task

import java.io.Serializable

data class AmountCurrency(

    val currency:String?,
    val amount:Double?
):Serializable
