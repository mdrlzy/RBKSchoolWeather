package com.mdrlzy.rbkweather.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OneCallResponseDto(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Int,
    val current: CurrentDto,
    val hourly: List<HourlyForecastDto> = emptyList(),
    val daily: List<DailyForecastDto> = emptyList(),
)

@Serializable
data class CurrentDto(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val uvi: Double,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDeg: Int,
    val weather: List<WeatherInfoDto> = emptyList(),
)

@Serializable
data class HourlyForecastDto(
    val dt: Long,
    val temp: Double,
    val pop: Double? = null,
    val weather: List<WeatherInfoDto> = emptyList(),
)

@Serializable
data class DailyForecastDto(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: DailyTempDto,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDeg: Int,
    val uvi: Double,
    val summary: String? = null,
    val weather: List<WeatherInfoDto> = emptyList(),
)

@Serializable
data class DailyTempDto(
    val min: Double,
    val max: Double,
    val day: Double? = null,
)

@Serializable
data class WeatherInfoDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)
