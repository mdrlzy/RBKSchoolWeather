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
    val allCities: List<CityWeatherCardUiItem>,
    val filteredCities: List<CityWeatherCardUiItem>,
    val searchQuery: String = "",
) {
    companion object {
        fun initial() = CityListScreenState(
            allCities = emptyList(),
            filteredCities = emptyList(),
        )
    }
}

sealed interface CityListEffect {
    data object ShowMenuBottomSheet : CityListEffect
    data object HideMenuBottomSheet : CityListEffect
}
