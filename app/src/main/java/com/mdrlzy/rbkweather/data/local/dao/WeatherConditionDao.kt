package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.WeatherConditionEntity

@Dao
interface WeatherConditionDao {
    @Query("SELECT * FROM WeatherConditionEntity ORDER BY ownerType ASC, ownerIndex ASC, position ASC")
    suspend fun getConditions(): List<WeatherConditionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertConditions(entities: List<WeatherConditionEntity>)

    @Query("DELETE FROM WeatherConditionEntity")
    suspend fun clearConditions()
}
