package com.empcloud.empmonitor.data.remote.response.clientfetch

import java.io.Serializable

data class ClientFetchDetail(
    val clientProfilePic:String,
    val _id:String,
    val clientName:String,
    val orgId:String,
    val emp_id:String,
    val clientId:String,
    val contactNumber:String,
    val countryCode:String?,
    val address1:String,
    val address2:String,
    val country:String,
    val state:String,
    val city:String,
    val zipCode:String,
    val latitude:String,
    val longitude:String,
    val clientType:Int,
    val status:String,
    val createdAt:String,
    val updatedAt:String,
    val emailId:String,
    val category:String
):Serializable
