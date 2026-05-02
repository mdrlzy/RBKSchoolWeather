package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.WeatherConditionEntity

@Dao
interface WeatherConditionDao {
    @Query(
        """
        SELECT * FROM WeatherConditionEntity
        WHERE currentWeatherLocationId = :locationId
        ORDER BY id ASC
        LIMIT 1
        """
    )
    suspend fun getCurrentCondition(locationId: Long): WeatherConditionEntity?

    @Query(
        """
        SELECT * FROM WeatherConditionEntity
        WHERE currentWeatherLocationId = :locationId
            OR hourlyWeatherId IN (
                SELECT id FROM HourlyWeatherEntity WHERE locationId = :locationId
            )
            OR dailyWeatherId IN (
                SELECT id FROM DailyWeatherEntity WHERE locationId = :locationId
            )
        ORDER BY id ASC
        """
    )
    suspend fun getConditions(locationId: Long): List<WeatherConditionEntity>

    @Insert
    suspend fun insertConditions(entities: List<WeatherConditionEntity>)
}
