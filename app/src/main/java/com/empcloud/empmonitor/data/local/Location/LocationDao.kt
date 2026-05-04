package com.empcloud.empmonitor.data.local.Location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.empcloud.empmonitor.data.remote.response.locationRoom.LocationEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity)

    @Query("SELECT * FROM location_table")
    suspend fun getAllLocations(): List<LocationEntity>

    @Query("DELETE FROM location_table WHERE id = :id")
    suspend fun deleteById(id: Int)

}