package com.mdrlzy.rbkweather.presentation.citylist

data class CityWeatherCardUiItem(
    val id: Long,
    val cityName: String?,
    val subtitle: String?,
    val condition: String?,
    val temperature: Int?,
    val minTemperature: Int?,
    val maxTemperature: Int?,
)

data class CityListScreenState(
    val cities: List<CityWeatherCardUiItem>,
) {
    companion object {
        fun initial() = CityListScreenState(cities = emptyList())
    }
}

sealed interface CityListEffect {
    data object ShowMenuBottomSheet : CityListEffect
    data object HideMenuBottomSheet : CityListEffect
}
