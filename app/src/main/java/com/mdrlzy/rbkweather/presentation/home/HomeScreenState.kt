package com.mdrlzy.rbkweather.presentation.home

import java.time.OffsetDateTime

data class HomeWeatherPageUiState(
    val city: String = "Алматы",
    val description: String = "В основном солнечно",
    val detailedDescription: String = "В основном солнечно",

    val hourlyItems: List<HourlyUiModel> = emptyList(),
    val dailyItems: List<DailyForecastUi> = emptyList(),

    val temp: Int = 11,
    val minTemp: Int = 5,
    val maxTemp: Int = 11,

    val feelsLike: Int = 12,
    val humidity: Int = 70,
    val pressure: Int = 1022,
    val uvIndex: Int = 0,

    val windSpeed: Int = 6,
    val windDirection: String = "",
    val windMaxSpeed: Int = 6,

    val sunsetTime: OffsetDateTime = OffsetDateTime.now(),
    val sunriseTime: OffsetDateTime = OffsetDateTime.now(),
)

data class HomeScreenState(
    val pages: List<HomeWeatherPageUiState> = emptyList(),
    val isRefreshing: Boolean = false,
    val isInitialized: Boolean = false,
)