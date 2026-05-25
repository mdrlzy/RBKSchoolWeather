package com.mdrlzy.rbkweather.presentation.citylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdrlzy.rbkweather.domain.model.CityWeatherSummary
import com.mdrlzy.rbkweather.domain.repository.CityLocationRepository
import com.mdrlzy.rbkweather.domain.repository.Preferences
import com.mdrlzy.rbkweather.domain.repository.WeatherRepository
import com.mdrlzy.rbkweather.presentation.mapper.toDisplayTemperature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class CityListViewModel(
    private val cityLocationRepository: CityLocationRepository,
    private val weatherRepository: WeatherRepository,
    private val preferences: Preferences,
) : ViewModel() {

    private val _state = MutableStateFlow(CityListScreenState.initial())
    val state: StateFlow<CityListScreenState> = _state.asStateFlow()

    private val _effect = Channel<CityListEffect>(Channel.BUFFERED)
    val effect: Flow<CityListEffect> = _effect.receiveAsFlow()

    private var cityWeatherSummaries: List<CityWeatherSummary> = emptyList()

    init {
        preferences.isCelciusNotFarenheit.onEach { isCelciusNotFarenheit ->
            updateCities(
                summaries = cityWeatherSummaries,
                isCelciusNotFarenheit = isCelciusNotFarenheit,
            )
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            cityLocationRepository.ensureDefaultCities()
            cityLocationRepository.getCities().forEach { city ->
                weatherRepository.getCurrent(city)
            }
            cityWeatherSummaries = cityLocationRepository.getCityWeatherSummaries()
            updateCities(
                summaries = cityWeatherSummaries,
                isCelciusNotFarenheit = _state.value.isCelciusNotFarenheit,
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

    fun onTemperatureUnitClick(isCelciusNotFarenheit: Boolean) {
        viewModelScope.launch {
            preferences.setIsCelciusNotFarenheit(isCelciusNotFarenheit)
            _effect.send(CityListEffect.HideMenuBottomSheet)
        }
    }

    override fun onCleared() {
        _effect.close()
        super.onCleared()
    }

    private fun updateCities(
        summaries: List<CityWeatherSummary>,
        isCelciusNotFarenheit: Boolean,
    ) {
        val allCities = summaries.map { summary ->
            summary.toUi(isCelciusNotFarenheit)
        }
        val filteredCities = allCities.filterByQuery(_state.value.searchQuery)

        _state.update {
            it.copy(
                allCities = allCities,
                filteredCities = filteredCities,
                isCelciusNotFarenheit = isCelciusNotFarenheit,
            )
        }
    }
}

private fun List<CityWeatherCardUiItem>.filterByQuery(query: String): List<CityWeatherCardUiItem> {
    if (query.isBlank()) return this

    val trimmedQuery = query.trim()
    return filter { city ->
        city.cityName?.contains(trimmedQuery, ignoreCase = true) == true
    }
}

private fun CityWeatherSummary.toUi(isCelciusNotFarenheit: Boolean): CityWeatherCardUiItem {
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
        temperature = currentTemp?.toDisplayTemperature(isCelciusNotFarenheit),
        minTemperature = minTemp?.toDisplayTemperature(isCelciusNotFarenheit),
        maxTemperature = maxTemp?.toDisplayTemperature(isCelciusNotFarenheit),
    )
}
