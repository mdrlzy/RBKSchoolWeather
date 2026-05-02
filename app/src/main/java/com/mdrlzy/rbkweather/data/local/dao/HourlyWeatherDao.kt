package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.HourlyWeatherEntity

@Dao
interface HourlyWeatherDao {
    @Query(
        """
        SELECT * FROM HourlyWeatherEntity
        WHERE locationId = :locationId
        ORDER BY dateTimeEpochSeconds ASC
        """
    )
    suspend fun getHourly(locationId: Long): List<HourlyWeatherEntity>

    @Insert
    suspend fun insertHourly(entities: List<HourlyWeatherEntity>): List<Long>

    @Query("DELETE FROM HourlyWeatherEntity WHERE locationId = :locationId")
    suspend fun clearHourly(locationId: Long)
}
