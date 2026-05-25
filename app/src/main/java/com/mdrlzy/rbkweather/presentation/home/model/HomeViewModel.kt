package com.mdrlzy.rbkweather.presentation.home.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdrlzy.rbkweather.domain.model.CityLocation
import com.mdrlzy.rbkweather.domain.model.OneCallWeather
import com.mdrlzy.rbkweather.domain.repository.CityLocationRepository
import com.mdrlzy.rbkweather.domain.repository.LocationRepository
import com.mdrlzy.rbkweather.domain.repository.Preferences
import com.mdrlzy.rbkweather.domain.repository.WeatherRepository
import com.mdrlzy.rbkweather.domain.usecase.SaveCurrentCityLocationUseCase
import com.mdrlzy.rbkweather.presentation.location.LocationPermissionHelper
import com.mdrlzy.rbkweather.presentation.mapper.toDisplayTemperature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class HomeViewModel(
    private val locationRepository: LocationRepository,
    private val locationPermissionHelper: LocationPermissionHelper,
    private val cityLocationRepository: CityLocationRepository,
    private val weatherRepository: WeatherRepository,
    private val saveCurrentCityLocationUseCase: SaveCurrentCityLocationUseCase,
    private val preferences: Preferences,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>(Channel.BUFFERED)
    val effect: Flow<HomeEffect> = _effect.receiveAsFlow()

    private var weatherPageSources: List<Pair<CityLocation, OneCallWeather>> = emptyList()

    init {
        preferences.isCelciusNotFarenheit.onEach { isCelcius ->
            _state.update { it.copy(isCelciusNotFarenheit = isCelcius) }
            updateWeatherPages()
        }.launchIn(viewModelScope)


        onRefresh()
    }

    fun onRefresh() {
        if (locationPermissionHelper.hasFineLocationPermission()) {
            loadWeather()
        } else {
            emitEffect(HomeEffect.RequestLocationPermission)
        }
    }

    fun onPermissionLocationResult(granted: Boolean) {
        if (granted) {
            viewModelScope.launch {
                saveCurrentCityLocationUseCase()

                loadWeather()
            }
        } else {
            emitEffect(HomeEffect.LocationPermissionDenied)
        }
    }

    override fun onCleared() {
        _effect.close()
        super.onCleared()
    }

    private fun loadWeather() {
        if (_state.value.isRefreshing) return

        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            cityLocationRepository.ensureDefaultCities()
            val cities = cityLocationRepository.getCities()
            val weatherSources = cities.map { city ->
                async {
                    weatherRepository
                        .getCurrent(city)
                        .getOrNull()
                        ?.let { weather -> city to weather }
                }
            }.awaitAll().filterNotNull()


            if (weatherSources.isNotEmpty()) {
                weatherPageSources = weatherSources
                _state.value = HomeScreenState(
                    pages = weatherSources.toHomeWeatherPageUiStates(_state.value.isCelciusNotFarenheit),
                    isRefreshing = false,
                    isInitialized = true,
                    isCelciusNotFarenheit = _state.value.isCelciusNotFarenheit,
                )
            } else {
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        isInitialized = false,
                    )
                }
                emitEffect(HomeEffect.WeatherLoadFailed)
            }
        }
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    private fun updateWeatherPages() {
        if (weatherPageSources.isEmpty()) return

        _state.update {
            it.copy(pages = weatherPageSources.toHomeWeatherPageUiStates(it.isCelciusNotFarenheit))
        }
    }
}

private fun List<Pair<CityLocation, OneCallWeather>>.toHomeWeatherPageUiStates(
    isCelciusNotFarenheit: Boolean,
): List<HomeWeatherPageUiState> {
    return map { (city, weather) ->
        weather.toHomeWeatherPageUiState(
            city = city,
            isCelciusNotFarenheit = isCelciusNotFarenheit,
        )
    }
}

private fun OneCallWeather.toHomeWeatherPageUiState(
    city: CityLocation,
    isCelciusNotFarenheit: Boolean,
): HomeWeatherPageUiState {
    val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dayFormatter = DateTimeFormatter.ofPattern("E", Locale("ru"))

    val hourlyItems = buildList {
        add(
            HourlyUiModel(
                hour = current.dateTime.format(hourFormatter),
                temperature = current.temp.toDisplayTemperature(isCelciusNotFarenheit),
                isCurrent = true,
            )
        )
        hourly.take(11).forEach { item ->
            add(
                HourlyUiModel(
                    hour = item.dateTime.format(hourFormatter),
                    temperature = item.temp.toDisplayTemperature(isCelciusNotFarenheit),
                )
            )
        }
    }

    val dailyItems = daily.take(8).mapIndexed { index, item ->
        DailyForecastUi(
            day = item.dateTime
                .format(dayFormatter)
                .replaceFirstChar { it.uppercase() },
            minTemp = item.temp.min.toDisplayTemperature(isCelciusNotFarenheit),
            maxTemp = item.temp.max.toDisplayTemperature(isCelciusNotFarenheit),
            isToday = index == 0,
        )
    }

    val firstDaily = daily.firstOrNull()
    val sunriseTime = current.sunrise ?: firstDaily?.sunrise ?: current.dateTime
    val sunsetTime = current.sunset ?: firstDaily?.sunset ?: current.dateTime

    val shortDescription = current.weather.firstOrNull()?.description?.takeIf { it.isNotBlank() }
        ?: firstDaily?.weather?.firstOrNull()?.description
        ?: ""

    return HomeWeatherPageUiState(
        city = city.name?.takeIf { it.isNotBlank() }
            ?: timezone.substringAfterLast('/').replace('_', ' '),
        description = shortDescription,
        detailedDescription = shortDescription,
        hourlyItems = hourlyItems,
        dailyItems = dailyItems,
        temp = current.temp.toDisplayTemperature(isCelciusNotFarenheit),
        minTemp = firstDaily?.temp?.min?.toDisplayTemperature(isCelciusNotFarenheit)
            ?: current.temp.toDisplayTemperature(isCelciusNotFarenheit),
        maxTemp = firstDaily?.temp?.max?.toDisplayTemperature(isCelciusNotFarenheit)
            ?: current.temp.toDisplayTemperature(isCelciusNotFarenheit),
        feelsLike = current.feelsLike.toDisplayTemperature(isCelciusNotFarenheit),
        humidity = current.humidity,
        pressure = current.pressure,
        uvIndex = current.uvIndex.roundToInt(),
        windSpeed = current.windSpeed.roundToInt(),
        windDirectionDegrees = current.windDeg,
        windMaxSpeed = daily.maxOfOrNull { it.windSpeed }?.roundToInt()
            ?: current.windSpeed.roundToInt(),
        sunsetTime = sunsetTime,
        sunriseTime = sunriseTime,
    )
}
