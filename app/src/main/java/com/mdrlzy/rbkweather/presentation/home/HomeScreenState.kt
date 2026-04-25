package com.mdrlzy.rbkweather.presentation.home

data class HomeScreenState(
    val city: String = "Алматы",
    val temp: Int = 11,
    val description: String = "В основном солнечно",
    val minTemp: Int = 5,
    val maxTemp: Int = 11,

    val feelsLike: Int = 12,
    val humidity: Int = 70,
    val windSpeed: Int = 6,
    val pressure: Int = 1022,

    val isRefreshing: Boolean = false
)