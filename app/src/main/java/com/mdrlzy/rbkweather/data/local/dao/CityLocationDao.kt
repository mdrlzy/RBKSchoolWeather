package com.mdrlzy.rbkweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdrlzy.rbkweather.data.local.entity.CityLocationEntity

@Dao
interface CityLocationDao {
    @Query("SELECT * FROM CityLocationEntity ORDER BY id ASC")
    suspend fun getCities(): List<CityLocationEntity>

    @Query(
        """
        SELECT * FROM CityLocationEntity
        WHERE ABS(latitude - :latitude) <= :coordinateTolerance
            AND ABS(longitude - :longitude) <= :coordinateTolerance
        ORDER BY ABS(latitude - :latitude) + ABS(longitude - :longitude)
        LIMIT 1
        """
    )
    suspend fun getCityByCoordinates(
        latitude: Double,
        longitude: Double,
        coordinateTolerance: Double = 0.01,
    ): CityLocationEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(entity: CityLocationEntity): Long
}
