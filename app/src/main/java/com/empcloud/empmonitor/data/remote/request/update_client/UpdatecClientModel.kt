package com.empcloud.empmonitor.data.remote.request.update_client

import java.io.Serializable

data class UpdatecClientModel(
    val clientName:String,
    val emailId:String,
    val contactNumber:String,
    val clientProfilePic:String?,
    val category:String,
    val countryCode:String?,
    val address1:String?,
    val address2:String?,
    val country:String?,
    val state:String,
    val city:String,
    val zipCode:String,
    val latitude:String,
    val longitude:String
    ):Serializable
