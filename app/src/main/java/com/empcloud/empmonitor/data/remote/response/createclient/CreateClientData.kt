package com.empcloud.empmonitor.data.remote.response.createclient

import java.io.Serializable

data class CreateClientData(
    val clientName:String,
    val emailId:String,
    val orgId:String,
    val contactNumber:String,
    val category:String,
    val countryCode:String,
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
    val clientId:String
) :Serializable
