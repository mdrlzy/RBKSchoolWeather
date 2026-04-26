package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.DailyWeatherEntity

@Dao
interface DailyWeatherDao {
    @Query("SELECT * FROM DailyWeatherEntity ORDER BY position ASC")
    suspend fun getDaily(): List<DailyWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDaily(entities: List<DailyWeatherEntity>)

    @Query("DELETE FROM DailyWeatherEntity")
    suspend fun clearDaily()
}
