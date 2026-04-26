package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.HourlyWeatherEntity

@Dao
interface HourlyWeatherDao {
    @Query("SELECT * FROM HourlyWeatherEntity ORDER BY position ASC")
    suspend fun getHourly(): List<HourlyWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHourly(entities: List<HourlyWeatherEntity>)

    @Query("DELETE FROM HourlyWeatherEntity")
    suspend fun clearHourly()
}
