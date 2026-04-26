package com.mdrlzy.rbkweather.data.remote.dto

data class OneCallResponseDto(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: CurrentDto,
    val hourly: List<HourlyForecastDto> = emptyList(),
    val daily: List<DailyForecastDto> = emptyList(),
)

data class CurrentDto(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val uvi: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: List<WeatherInfoDto> = emptyList(),
)

data class HourlyForecastDto(
    val dt: Long,
    val temp: Double,
    val pop: Double? = null,
    val weather: List<WeatherInfoDto> = emptyList(),
)

data class DailyForecastDto(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: DailyTempDto,
    val wind_speed: Double,
    val wind_deg: Int,
    val uvi: Double,
    val summary: String? = null,
    val weather: List<WeatherInfoDto> = emptyList(),
)

data class DailyTempDto(
    val min: Double,
    val max: Double,
    val day: Double? = null,
)

data class WeatherInfoDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)
