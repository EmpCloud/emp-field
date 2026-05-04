package com.empcloud.empmonitor.data.remote.response.fetchattendacne

import com.empcloud.empmonitor.data.remote.response.markattendance.AttInnerData
import java.io.Serializable

data class AttendanceDataResponse(
    val orglatitude:String,
    val orglongitude:String,
    val isGeoFencingOn:Int,
    val isMobileDeviceEnabled:Int,
    val isBioMetricEnabled:Int,
    val currentFrequency:Int,
    val currentRadius:Int,
    val orgRadius:Float,
    val isWebEnabled:Int,
    val yesterdayDist:String,
    val yesterdaytask:String,
    val yesterdayHrs:String,
    val data:AttendanceListData,
    val geoLogStatus:Boolean,
    val currentMode:String,
    val employeeLocations: List<EmployeeLocation>? = null
):Serializable
