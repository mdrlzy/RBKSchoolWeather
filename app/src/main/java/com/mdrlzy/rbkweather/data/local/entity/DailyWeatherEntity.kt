package com.mdrlzy.rbkweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val position: Int,
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
