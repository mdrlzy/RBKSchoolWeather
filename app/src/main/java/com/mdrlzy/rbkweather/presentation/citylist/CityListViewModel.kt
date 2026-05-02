package com.mdrlzy.rbkweather.presentation.citylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdrlzy.rbkweather.domain.model.CityWeatherSummary
import com.mdrlzy.rbkweather.domain.repository.CityLocationRepository
import com.mdrlzy.rbkweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class CityListViewModel(
    private val cityLocationRepository: CityLocationRepository,
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CityListScreenState.initial())
    val state: StateFlow<CityListScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            cityLocationRepository.ensureDefaultCities()
            cityLocationRepository.getCities().forEach { city ->
                weatherRepository.getCurrent(city)
            }
            val cities = cityLocationRepository.getCityWeatherSummaries()
            _state.value = CityListScreenState(cities = cities.map { it.toUi() })
        }
    }
}

private fun CityWeatherSummary.toUi(): CityWeatherCardUiItem {
    val offset = timezoneOffsetSeconds?.let(ZoneOffset::ofTotalSeconds)
    val subtitle = if (currentDateTimeEpochSeconds != null && offset != null) {
        DateTimeFormatter.ofPattern("HH:mm")
            .format(Instant.ofEpochSecond(currentDateTimeEpochSeconds).atOffset(offset))
    } else {
        "Нет данных"
    }
    val currentTempText = currentTemp?.roundToInt()?.let { "$it°" } ?: "--"
    val minText = minTemp?.roundToInt()?.let { "$it°" } ?: "--"
    val maxText = maxTemp?.roundToInt()?.let { "$it°" } ?: "--"

    return CityWeatherCardUiItem(
        id = id,
        cityName = cityName ?: "Без названия",
        subtitle = subtitle,
        condition = currentConditionDescription ?: "Нет данных о погоде",
        temperature = currentTempText,
        temperatureRange = "Макс.: $maxText, мин.: $minText",
    )
}
