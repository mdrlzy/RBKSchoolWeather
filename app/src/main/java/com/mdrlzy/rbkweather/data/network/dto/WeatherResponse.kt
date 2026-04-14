package com.mdrlzy.rbkweather.data.network.dto

data class WeatherResponse(
    val main: Main,
    val wind: Wind,
    val weather: List<WeatherDto>
)

data class Main(
    val temp: Double,
    val feels_like: Double
)

data class Wind(
    val speed: Double
)

data class WeatherDto(
    val description: String
)