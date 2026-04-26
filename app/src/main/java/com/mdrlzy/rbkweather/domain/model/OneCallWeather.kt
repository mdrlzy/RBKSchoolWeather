package com.mdrlzy.rbkweather.domain.model

import java.time.OffsetDateTime

data class OneCallWeather(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezoneOffsetSeconds: Int,
    val current: CurrentWeather,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>,
)

data class CurrentWeather(
    val dateTime: OffsetDateTime,
    val sunrise: OffsetDateTime?,
    val sunset: OffsetDateTime?,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val uvIndex: Double,
    val windSpeed: Double,
    val windDeg: Int,
    val weather: List<WeatherCondition>,
)

data class HourlyWeather(
    val dateTime: OffsetDateTime,
    val temp: Double,
    val precipitationProbability: Double?,
    val weather: List<WeatherCondition>,
)

data class DailyWeather(
    val dateTime: OffsetDateTime,
    val sunrise: OffsetDateTime?,
    val sunset: OffsetDateTime?,
    val temp: DailyTemp,
    val windSpeed: Double,
    val windDeg: Int,
    val uvIndex: Double,
    val summary: String?,
    val weather: List<WeatherCondition>,
)

data class DailyTemp(
    val min: Double,
    val max: Double,
    val day: Double?,
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)
