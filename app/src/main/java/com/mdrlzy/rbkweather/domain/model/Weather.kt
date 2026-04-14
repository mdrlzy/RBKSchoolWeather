package com.mdrlzy.rbkweather.domain.model

data class Weather(
    val temperature: Int,
    val feelsLike: Int,
    val description: String,
    val windSpeed: Double
)