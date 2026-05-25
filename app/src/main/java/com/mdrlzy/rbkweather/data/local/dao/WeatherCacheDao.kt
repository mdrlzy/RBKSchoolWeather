package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.WeatherCacheEntity

@Dao
interface WeatherCacheDao {
    @Query("SELECT * FROM WeatherCacheEntity WHERE locationId = :locationId LIMIT 1")
    suspend fun getCache(locationId: Long): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCache(entity: WeatherCacheEntity)

    @Query("DELETE FROM WeatherCacheEntity WHERE locationId = :locationId")
    suspend fun clearCache(locationId: Long)
}
