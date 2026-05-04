package com.empcloud.empmonitor.network.api_satatemanagement

import okhttp3.ResponseBody

sealed class ApiState<out T> {
    data class SUCESS<out R>(val getResponse: R): ApiState<R>()
    data class ERROR(val message:String,val isNetworkERROR: Boolean,
                     val errorCode:Int?,
                     val errorBody: ResponseBody?): ApiState<Nothing>()
    object LOADING: ApiState<Nothing>()

}
