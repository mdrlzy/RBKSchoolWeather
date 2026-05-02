package com.mdrlzy.rbkweather.presentation.citylist

data class CityWeatherCardUiItem(
    val id: Long,
    val cityName: String,
    val subtitle: String,
    val condition: String,
    val temperature: String,
    val temperatureRange: String,
)

data class CityListScreenState(
    val cities: List<CityWeatherCardUiItem>,
) {
    companion object {
        fun initial() = CityListScreenState(cities = emptyList())
    }
}
