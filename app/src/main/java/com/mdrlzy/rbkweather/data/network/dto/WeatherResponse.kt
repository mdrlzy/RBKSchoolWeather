package com.mdrlzy.rbkweather.data.network.dto

data class WeatherResponse(
    val name: String,
    val main: MainDto,
    val wind: WindDto,
    val weather: List<WeatherDto>
)

data class MainDto(
    val temp: Double,
    val feels_like: Double
)

data class WindDto(
    val speed: Double
)

data class WeatherDto(
    val description: String
)