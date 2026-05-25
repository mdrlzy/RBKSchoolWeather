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
            childColumns = ["currentWeatherLocationId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = HourlyWeatherEntity::class,
            parentColumns = ["id"],
            childColumns = ["hourlyWeatherId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = DailyWeatherEntity::class,
            parentColumns = ["id"],
            childColumns = ["dailyWeatherId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["currentWeatherLocationId"]),
        Index(value = ["hourlyWeatherId"]),
        Index(value = ["dailyWeatherId"]),
    ],
)
data class WeatherConditionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val currentWeatherLocationId: Long? = null,
    val hourlyWeatherId: Long? = null,
    val dailyWeatherId: Long? = null,
    val weatherId: Int,
    val main: String,
    val description: String,
    val icon: String,
)
