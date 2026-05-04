package com.empcloud.empmonitor.data.remote.response.locationRoom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationEntity(

    @PrimaryKey(autoGenerate = true) val id :Int = 0,
    val latitude:Double,
    val longitude:Double,
    val timeStamp:String
)
