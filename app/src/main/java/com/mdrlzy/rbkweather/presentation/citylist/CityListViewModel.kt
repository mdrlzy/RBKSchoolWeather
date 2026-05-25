package com.mdrlzy.rbkweather.presentation.citylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdrlzy.rbkweather.domain.model.CityWeatherSummary
import com.mdrlzy.rbkweather.domain.repository.CityLocationRepository
import com.mdrlzy.rbkweather.domain.repository.WeatherRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _effect = Channel<CityListEffect>(Channel.BUFFERED)
    val effect: Flow<CityListEffect> = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            cityLocationRepository.ensureDefaultCities()
            cityLocationRepository.getCities().forEach { city ->
                weatherRepository.getCurrent(city)
            }
            val cities = cityLocationRepository.getCityWeatherSummaries()
            val allCities = cities.map { it.toUi() }
            _state.value = _state.value.copy(
                allCities = allCities,
                filteredCities = allCities,
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        val allCities = _state.value.allCities
        val filteredCities = if (query.isBlank()) {
            allCities
        } else {
            val trimmedQuery = query.trim()
            allCities.filter { city ->
                city.cityName?.contains(trimmedQuery, ignoreCase = true) == true
            }
        }

        _state.value = _state.value.copy(
            filteredCities = filteredCities,
            searchQuery = query,
        )
    }

    fun onMoreClick() {
        viewModelScope.launch {
            _effect.send(CityListEffect.ShowMenuBottomSheet)
        }
    }

    fun onMenuDismiss() {
        viewModelScope.launch {
            _effect.send(CityListEffect.HideMenuBottomSheet)
        }
    }

    override fun onCleared() {
        _effect.close()
        super.onCleared()
    }
}

private fun CityWeatherSummary.toUi(): CityWeatherCardUiItem {
    val offset = timezoneOffsetSeconds?.let(ZoneOffset::ofTotalSeconds)
    val subtitle = if (currentDateTimeEpochSeconds != null && offset != null) {
        DateTimeFormatter.ofPattern("HH:mm")
            .format(Instant.ofEpochSecond(currentDateTimeEpochSeconds).atOffset(offset))
    } else {
        null
    }

    return CityWeatherCardUiItem(
        id = id,
        cityName = cityName,
        subtitle = subtitle,
        condition = currentConditionDescription,
        temperature = currentTemp?.roundToInt(),
        minTemperature = minTemp?.roundToInt(),
        maxTemperature = maxTemp?.roundToInt(),
    )
}
