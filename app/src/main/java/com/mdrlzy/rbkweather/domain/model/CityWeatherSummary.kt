package com.mdrlzy.rbkweather.domain.model

data class CityWeatherSummary(
    val id: Long,
    val cityName: String?,
    val timezoneOffsetSeconds: Int?,
    val currentDateTimeEpochSeconds: Long?,
    val currentTemp: Double?,
    val currentConditionDescription: String?,
    val minTemp: Double?,
    val maxTemp: Double?,
)
