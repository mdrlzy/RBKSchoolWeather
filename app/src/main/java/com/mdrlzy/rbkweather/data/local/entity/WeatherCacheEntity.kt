package com.mdrlzy.rbkweather.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CityLocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["latitude", "longitude"])],
)
data class WeatherCacheEntity(
    @PrimaryKey val locationId: Long,
    val cachedAtEpochMillis: Long,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezoneOffsetSeconds: Int,
    val currentDateTimeEpochSeconds: Long,
    val currentSunriseEpochSeconds: Long?,
    val currentSunsetEpochSeconds: Long?,
    val currentTemp: Double,
    val currentFeelsLike: Double,
    val currentPressure: Int,
    val currentHumidity: Int,
    val currentUvIndex: Double,
    val currentWindSpeed: Double,
    val currentWindDeg: Int,
)
