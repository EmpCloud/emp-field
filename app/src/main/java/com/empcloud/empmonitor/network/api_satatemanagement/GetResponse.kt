package com.empcloud.empmonitor.network.api_satatemanagement

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response


object GetResponse {
    fun <T> fromFlow(safeApiCall: suspend () -> Response<T>): Flow<ApiState<T>> = flow {
        emit(ApiState.LOADING)
        try {
            val response = safeApiCall()


            Log.d("EXCEPTION_FROM_LOGIN_API" , response.isSuccessful.toString())
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ApiState.SUCESS(it))
                }
            } else {
                response.errorBody()?.let { error ->
                    error.close()
                    emit(
                        ApiState.ERROR(
                            message = error.toString(),
                            isNetworkERROR = false,
                            errorCode = response.code(),
                            errorBody = response.errorBody()
                        )
                    )
                }
            }
        }catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    Log.d("EXCEPTION_FROM_LOGIN_API_ERR" , throwable.code().toString())

                    ApiState.ERROR(
                        message = throwable.message!!,
                        isNetworkERROR = false,
                        errorCode = throwable.code(),
                        errorBody = throwable.response()?.errorBody()
                    )
                }
                else -> {
                    Log.d("EXCEPTION_FROM_LOGIN_API_ERROR" , throwable.message!!.toString())

                    ApiState.ERROR(
                        message = throwable.message!!,
                        isNetworkERROR = true,
                        errorCode = null,
                        errorBody = null
                    )
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}