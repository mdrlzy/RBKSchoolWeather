package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.DailyWeatherEntity

@Dao
interface DailyWeatherDao {
    @Query(
        """
        SELECT * FROM DailyWeatherEntity
        WHERE locationId = :locationId
        ORDER BY dateTimeEpochSeconds ASC
        LIMIT 1
        """
    )
    suspend fun getFirstDaily(locationId: Long): DailyWeatherEntity?

    @Query(
        """
        SELECT * FROM DailyWeatherEntity
        WHERE locationId = :locationId
        ORDER BY dateTimeEpochSeconds ASC
        """
    )
    suspend fun getDaily(locationId: Long): List<DailyWeatherEntity>

    @Insert
    suspend fun insertDaily(entities: List<DailyWeatherEntity>): List<Long>

    @Query("DELETE FROM DailyWeatherEntity WHERE locationId = :locationId")
    suspend fun clearDaily(locationId: Long)
}
