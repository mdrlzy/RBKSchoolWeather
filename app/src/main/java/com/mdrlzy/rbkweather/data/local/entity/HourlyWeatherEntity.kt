package com.mdrlzy.rbkweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HourlyWeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val position: Int,
    val dateTimeEpochSeconds: Long,
    val temp: Double,
    val precipitationProbability: Double?,
)
