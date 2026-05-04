package com.empcloud.empmonitor.data.remote.request.updateprofile

import java.io.Serializable

data class UpdateProfileModel(

    val fullName:String?,
    val age:String?,
    val gender:String?,
    val email:String?,
    val profilePic:String?,
    val address1:String?,
    val address2:String?,
    val latitude:String?,
    val longitude:String?,
    val city:String?,
    val state:String?,
    val country:String?,
    val zipCode:String?,
    val phoneNumber:String?
    ):Serializable
