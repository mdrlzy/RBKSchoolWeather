package com.mdrlzy.rbkweather.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WeatherCacheEntity::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["locationId"])],
)
data class DailyWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val locationId: Long,
    val dateTimeEpochSeconds: Long,
    val sunriseEpochSeconds: Long?,
    val sunsetEpochSeconds: Long?,
    val minTemp: Double,
    val maxTemp: Double,
    val dayTemp: Double?,
    val windSpeed: Double,
    val windDeg: Int,
    val uvIndex: Double,
    val summary: String?,
)
