package com.empcloud.empmonitor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.empcloud.empmonitor.data.local.Location.LocationDao
import com.empcloud.empmonitor.data.remote.response.locationRoom.LocationEntity

@Database(entities = [LocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}