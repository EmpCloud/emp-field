package com.empcloud.empmonitor.data.remote.request.createclient

import java.io.Serializable

data class CreateClientModel(
    val clientName:String,
    val emailId:String?,
    val contactNumber:String,
    val category:String,
    val countryCode:String?,
    val address1:String,
    val address2:String,
    val country:String?,
    val state:String,
    val city:String,
    val zipCode:String,
    val latitude: Double,
    val longitude: Double,
    val clientProfilePic:String?
    ):Serializable
