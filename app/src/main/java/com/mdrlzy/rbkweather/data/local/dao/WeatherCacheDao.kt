package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.WeatherCacheEntity

@Dao
interface WeatherCacheDao {
    @Query("SELECT * FROM WeatherCacheEntity LIMIT 1")
    suspend fun getCache(): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCache(entity: WeatherCacheEntity)

    @Query("DELETE FROM WeatherCacheEntity")
    suspend fun clearCache()
}
