package com.mdrlzy.rbkweather.presentation.home.model

data class DailyForecastUi(
    val day: String,
    val minTemp: Int,
    val maxTemp: Int,
    val isToday: Boolean = false,
)

data class HourlyUiModel(
    val hour: String,
    val temperature: Int,
    val isCurrent: Boolean = false,
)